package com.antsol.op.gui.asset;

import com.antsol.op.asset.Asset;
import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.gui.market.MarketController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.antsol.op.gui.common.ControllerUtils.initializePlotStage;

@Slf4j
public abstract class AssetController<A extends Asset> extends AbstractController<A> {

    @Setter
    protected MarketController<A> marketController;

    private XYChart.Series<Number, Number> scaleSeries;

    private Stage pricePlotStage;
    private XYChart.Series<Number, Number> priceSeries;

    @FXML
    private Label nameLabel;
    @FXML
    private Label openingPriceLabel;
    @FXML
    private Label minPriceLabel;
    @FXML
    private Label maxPriceLabel;
    @FXML
    private Label currentPriceLabel;

    public static <A extends Asset> EventHandler<? super MouseEvent> onAssetMouseClick(
            final MarketController<A> marketController, final ListView<A> assets) {
        return (x) -> {
            if (!assets.getSelectionModel().isEmpty()) {
                try {
                    String resourcePath = "/asset/%s-view.fxml";
                    switch (marketController.getItem().getType()) {
                        case STOCK -> resourcePath = String.format(resourcePath, "company");
                        case CURRENCY -> resourcePath = String.format(resourcePath, "currency-asset");
                        case COMMODITY -> resourcePath = String.format(resourcePath, "commodity");
                    }
                    final A asset = assets.getSelectionModel().getSelectedItem();
                    ControllerUtils.<A, AssetController<A>>buildItemStage(
                            asset, resourcePath, y -> y.setMarketController(marketController));
                } catch (Exception ignored) {
                }
            }
        };
    }

    private void initPricePlot() {
        final LineChart<Number, Number> priceChart = ControllerUtils.initializeLineChart(
                item + " price over time", "time", "price");
        priceSeries = new XYChart.Series<>();
        priceSeries.setName(item.getAssetName());
        priceChart.getData().add(priceSeries);
        pricePlotStage = initializePlotStage(priceChart);
    }

    private void initScalePlot() {
        scaleSeries = new XYChart.Series<>();
        scaleSeries.setName(item.getAssetName());
    }

    @Override
    protected void init() {
        final Optional<XYChart.Series<Number, Number>> series = marketController
                .getScaleChart().getData().stream()
                .filter(x -> x.getName().equals(item.getAssetName())).findAny();
        if (series.isEmpty()) {
            initScalePlot();
        } else {
            scaleSeries = series.get();
        }
        initPricePlot();
        nameLabel.setText(item.getAssetName());
        openingPriceLabel.setText(String.valueOf(item.getOpeningPrice()));
        final Thread refresher = ControllerUtils.createAndStartRefresher(this::refresh, item.toString(),
                marketController.getMainController().getItem().getSettings().getTimeStep());
        stage.setOnCloseRequest(x -> refresher.interrupt());
    }

    @FXML
    private void onScalePlotButtonClick() {
        if (!marketController.getScaleChart().getData().contains(scaleSeries)) {
            marketController.getScaleChart().getData().add(scaleSeries);
        }
        marketController.showPlot();
    }

    @FXML
    private void onPricePlotButtonClick() {
        if (!pricePlotStage.isShowing()) {
            pricePlotStage.show();
        }
    }

    @FXML
    private void onClearButtonClick() {
        marketController.getScaleChart().getData().remove(scaleSeries);
    }

    @FXML
    private void onRemoveButtonClick() {
        marketController.getItem().removeAsset(item);
        marketController.getScaleChart().getData().remove(scaleSeries);
        marketController.refresh();
        stage.close();
    }

    private synchronized void refreshScalePlot() {
        for (int i = scaleSeries.getData().size(); i < item.getHistory().size(); i++) {
            scaleSeries.getData().add(new XYChart.Data<>(item.getHistory().get(i).getTimestamp().getTime(), item.getHistory().get(i).getValue() / item.getOpeningPrice()));
        }
    }

    private synchronized void refreshPricePlot() {
        for (int i = priceSeries.getData().size(); i < item.getHistory().size(); i++) {
            priceSeries.getData().add(new XYChart.Data<>(item.getHistory().get(i).getTimestamp().getTime(), item.getHistory().get(i).getValue()));
        }
    }

    protected void refresh() {
        refreshScalePlot();
        refreshPricePlot();
        minPriceLabel.setText(String.valueOf(item.getMinPrice()));
        maxPriceLabel.setText(String.valueOf(item.getMaxPrice()));
        currentPriceLabel.setText(String.valueOf(item.getCurrentPrice()));
    }

}
