package com.antsol.op.gui.market;

import com.antsol.op.asset.Asset;
import com.antsol.op.gui.admin.MainController;
import com.antsol.op.gui.asset.AssetController;
import com.antsol.op.gui.asset.CreateAssetController;
import com.antsol.op.gui.asset.CreateCurrencyAssetController;
import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.market.Market;
import com.antsol.op.market.StockMarket;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.antsol.op.gui.common.ControllerUtils.initializeLineChart;
import static com.antsol.op.gui.common.ControllerUtils.initializePlotStage;

public class MarketController<A extends Asset> extends AbstractController<Market<A>> {

    @Setter
    @Getter
    private MainController mainController;

    private Stage scalePlotStage;
    @Getter
    private LineChart<Number, Number> scaleChart;

    @FXML
    private Label typeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label currencyLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label marginLabel;
    @FXML
    private ListView<A> assetsListView;

    public static EventHandler<? super MouseEvent> onMarketMouseClick(
            final MainController mainController, final ListView<Market<? extends Asset>> markets) {
        return (x) -> {
            if (!markets.getSelectionModel().isEmpty()) {
                try {
                    final Market<? extends Asset> market = markets.getSelectionModel().getSelectedItem();
                    final String resourcePath = (market instanceof StockMarket)
                            ? "/market/stock-market-view.fxml"
                            : "/market/market-view.fxml";
                    buildItemStage(market, resourcePath, mainController);
                } catch (Exception ignored) {
                }
            }
        };
    }

    private static <A extends Asset> void buildItemStage(final Market<A> market, final String resourcePath,
                                                         final MainController mainController) throws IOException {
        ControllerUtils.<Market<A>, MarketController<A>>buildItemStage(market, resourcePath,
                y -> {
                    y.setMainController(mainController);
                    if (market instanceof StockMarket)
                        ((StockMarketController) y).indexListView.setItems(FXCollections.observableList(((StockMarket) market).getIndexes()));
                });
    }

    @Override
    protected void init() {
        typeLabel.setText(item.getType().name() + " MARKET");
        nameLabel.setText(item.getName());
        currencyLabel.setText(item.getCurrency().toString());
        addressLabel.setText(item.getAddress() + " " + item.getCity() + ", " + item.getCountry());
        marginLabel.setText(String.valueOf(item.getMargin()));
        refresh();
        scaleChart = initializeLineChart("stock price", "time", "current / opening");
        scalePlotStage = initializePlotStage(scaleChart);
        assetsListView.setOnMouseClicked(AssetController.onAssetMouseClick(this, assetsListView));
    }

    @FXML
    private void onCreateButtonClick() throws IOException {
        String resourcePath = "/asset/create-%s-view.fxml";
        switch (item.getType()) {
            case STOCK -> resourcePath = String.format(resourcePath, "company");
            case CURRENCY -> resourcePath = String.format(resourcePath, "currency-asset");
            case COMMODITY -> resourcePath = String.format(resourcePath, "commodity");
        }
        ControllerUtils.<A, CreateAssetController<A>>buildCreateDialog(
                resourcePath,
                x -> {
                    if (x instanceof CreateCurrencyAssetController) {
                        ((CreateCurrencyAssetController) x).setCurrencies(mainController.getItem().getCurrencies()
                                .stream().filter(y -> !y.equals(item.getCurrency())).collect(Collectors.toList()));
                    }
                    x.setMarket(item);
                },
                x -> {
                    item.createAsset(x);
                    refresh();
                }
        );
    }

    @FXML
    private void onRemoveButtonClick() {
        mainController.getItem().removeMarket(item);
        mainController.refresh();
        stage.close();
    }

    public void refresh() {
        assetsListView.setItems(FXCollections.observableList(item.getAssetsList()));
    }

    public void showPlot() {
        if (!scalePlotStage.isShowing()) {
            scalePlotStage.show();
        }
    }

}
