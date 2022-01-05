package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.market.MarketController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

public abstract class AssetController<A extends Asset<A>> extends AbstractController<A> {

    @Setter
    protected MarketController<A> marketController;

    protected XYChart.Series<Number, Number> series;
    protected int it;

    public static <A extends Asset<A>> EventHandler<? super MouseEvent> onAssetMouseClick(final MarketController<A> marketController, final ListView<A> assets) {
        return (x) -> {
            if (!assets.getSelectionModel().isEmpty()) {
                try {
                    String resourcePath = "/asset/%s-view.fxml";
                    switch (marketController.getItem().getType()) {
                        case STOCK -> resourcePath = String.format(resourcePath, "company");
                        case CURRENCY -> resourcePath = String.format(resourcePath, "currency-asset");
                        case COMMODITY -> resourcePath = String.format(resourcePath, "commodity");
                    }
                    final Stage stage = new Stage();
                    final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourcePath));
                    stage.setScene(new Scene(fxmlLoader.load()));
                    final AssetController<A> controller = fxmlLoader.getController();
                    controller.setMarketController(marketController);
                    controller.setItem(assets.getSelectionModel().getSelectedItem());
                    controller.setStage(stage);
                    marketController.getControllers().add(controller);
                    stage.show();
                } catch (Exception ignored) {}
            }
        };
    }

    @Override
    protected void init() {
        series = new XYChart.Series<>();
        series.setName(item.getAssetName());
        marketController.getLineChart().getData().add(series);
        it = 0;
        nameLabel.setText(item.getAssetName());
        openingPriceLabel.setText(String.valueOf(item.getOpeningPrice()));
        refresh();
    }

    @FXML protected Label nameLabel;
    @FXML protected Label openingPriceLabel;
    @FXML protected Label minPriceLabel;
    @FXML protected Label maxPriceLabel;
    @FXML protected Label currentPriceLabel;

    @FXML protected void onPlotButtonClick() {
        refreshPlot();
        series.getNode().setVisible(true);
        marketController.showPlot();
    }

    @FXML protected void onClearButtonClick() {
        series.getNode().setVisible(false);
    }

    @FXML protected void onRefreshButtonClick() {
        refreshPlot();
        refresh();
    }

    @FXML protected void onRemoveButtonClick() {
        marketController.getItem().removeAsset(item);
        marketController.getControllers().remove(this);
        marketController.getLineChart().getData().remove(series);
        marketController.refresh();
        stage.close();
    }

    public void refreshPlot() {
        synchronized (new Object()) {
            for (; it < item.getHistory().size(); it++) {
                series.getData().add(new XYChart.Data<>(item.getHistory().get(it).getTimestamp().getTime(), item.getHistory().get(it).getValue()));
            }
        }
    }

    protected void refresh() {
        minPriceLabel.setText(String.valueOf(item.getMinPrice()));
        maxPriceLabel.setText(String.valueOf(item.getMaxPrice()));
        currentPriceLabel.setText(String.valueOf(item.getCurrentPrice()));
    }

}
