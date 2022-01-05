package com.put.solarsan.op.gui.market;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.MainController;
import com.put.solarsan.op.gui.asset.AssetController;
import com.put.solarsan.op.gui.asset.CreateAssetController;
import com.put.solarsan.op.gui.asset.CreateCurrencyAssetController;
import com.put.solarsan.op.market.Market;
import com.put.solarsan.op.market.StockMarket;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.put.solarsan.op.gui.common.ControllerUtils.*;

public class MarketController<A extends Asset<A>> extends AbstractController<Market<A>> {

    @Setter
    protected MainController mainController;
    protected Stage plotStage;
    @Getter
    protected LineChart<Number, Number> lineChart;
    @Getter
    protected List<AssetController<A>> controllers;

    public static EventHandler<? super MouseEvent> onMarketMouseClick(final MainController mainController, final ListView<Market<?>> markets) {
        return (x) -> {
            if (!markets.getSelectionModel().isEmpty()) {
                try {
                    final Market<?> market = markets.getSelectionModel().getSelectedItem();
                    final URL resource = (market instanceof StockMarket)
                            ? MainApplication.class.getResource("/market/stock-market-view.fxml")
                            : MainApplication.class.getResource("/market/market-view.fxml");
                    final Stage stage = new Stage();
                    final FXMLLoader fxmlLoader = new FXMLLoader(resource);
                    stage.setScene(new Scene(fxmlLoader.load()));
                    final MarketController controller = fxmlLoader.getController();
                    controller.setMainController(mainController);
                    controller.setItem(market);
                    controller.setStage(stage);
                    if (market instanceof StockMarket) {
                        ((StockMarketController) controller).indexListView.setItems(
                                FXCollections.observableList(((StockMarket) market).getIndexes())
                        );
                    }
                    stage.show();
                } catch (Exception ignored) {}
            }
        };
    }

    @FXML protected Label typeLabel;
    @FXML protected Label nameLabel;
    @FXML protected Label currencyLabel;
    @FXML protected Label addressLabel;
    @FXML protected Label marginLabel;
    @FXML protected ListView<A> assetsListView;

    @Override
    protected void init() {
        typeLabel.setText(item.getType().name() + " MARKET");
        nameLabel.setText(item.getName());
        currencyLabel.setText(item.getCurrency().toString());
        addressLabel.setText(item.getAddress() + " " + item.getCity() + ", " + item.getCountry());
        marginLabel.setText(String.valueOf(item.getMargin()));
        refresh();
        lineChart = initializeLineChart("stock price");
        plotStage = initializePlotStage(lineChart, this::refreshPlot);
        assetsListView.setOnMouseClicked(AssetController.onAssetMouseClick(this, assetsListView));
        controllers = new ArrayList<>();
    }

    @FXML protected void onCreateButtonClick() throws IOException {
        String resourcePath = "/asset/create-%s-view.fxml";
        switch (item.getType()) {
            case STOCK -> resourcePath = String.format(resourcePath, "company");
            case CURRENCY -> resourcePath = String.format(resourcePath, "currency-asset");
            case COMMODITY -> resourcePath = String.format(resourcePath, "commodity");
        }
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourcePath));
        final DialogPane dialogPane = fxmlLoader.load();
        final CreateAssetController<A> controller = fxmlLoader.getController();
        if (controller instanceof CreateCurrencyAssetController) {
            ((CreateCurrencyAssetController) controller).setCurrencies(mainController.getItem().getCurrencies()
                    .stream().filter(x -> !x.equals(item.getCurrency())).collect(Collectors.toList()));
        }
        showDialog(dialogPane, simpleOkCallback(() -> {
            controller.setSettings(mainController.getItem().getSettings());
            item.createAsset(controller.create());
            refresh();
        }));
    }

    @FXML protected void onRemoveButtonClick() {
        mainController.getItem().removeMarket(item);
        mainController.refresh();
        stage.close();
    }

    public void refresh() {
        assetsListView.setItems(FXCollections.observableList(item.getAssetsList()));
    }

    public void showPlot() {
        if (!plotStage.isShowing()) {
            plotStage.show();
        }
    }

    public void refreshPlot() {
        this.controllers.forEach(AssetController::refreshPlot);
    }

}
