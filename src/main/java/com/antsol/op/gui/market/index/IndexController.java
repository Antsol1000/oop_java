package com.antsol.op.gui.market.index;

import com.antsol.op.asset.Company;
import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.gui.market.StockMarketController;
import com.antsol.op.market.StockMarket;
import com.antsol.op.market.index.Index;
import com.antsol.op.market.index.MarketIndex;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.util.Optional;

public class IndexController extends AbstractController<Index> {

    @Setter
    private StockMarketController marketController;

    private XYChart.Series<Number, Number> series;
    @FXML
    private Label nameLabel;
    @FXML
    private Label currentValueLabel;
    @FXML
    private ListView<Company> companyListView;

    public static EventHandler<? super MouseEvent> onIndexMouseClick(final StockMarketController marketController, final ListView<Index> indexes) {
        return (x) -> {
            if (!indexes.getSelectionModel().isEmpty()) {
                try {
                    final Index index = indexes.getSelectionModel().getSelectedItem();
                    IndexController controller = ControllerUtils.buildItemStage(
                            index, "/index/index-view.fxml", y -> y.setMarketController(marketController));
                    marketController.getIndexControllers().add(controller);
                } catch (Exception ignored) {
                }
            }
        };
    }

    private void initPlot() {
        series = new XYChart.Series<>();
        series.setName(item.toString());
        marketController.getIndexLineChart().getData().add(series);
    }

    @Override
    protected void init() {
        final Optional<XYChart.Series<Number, Number>> any = marketController
                .getIndexLineChart().getData().stream()
                .filter(x -> x.getName().equals(item.getName())).findAny();
        if (any.isEmpty()) {
            initPlot();
        } else {
            series = any.get();
        }
        nameLabel.setText(item.toString());
        companyListView.setItems(FXCollections.observableList(item.getCompanies()));
        final Thread refresher = ControllerUtils.createAndStartRefresher(this::refresh, item.toString(),
                marketController.getMainController().getItem().getSettings().getTimeStep());
        stage.setOnCloseRequest(x -> refresher.interrupt());
    }

    @FXML
    private void onPlotButtonClick() {
        if (!marketController.getIndexLineChart().getData().contains(series)) {
            marketController.getIndexLineChart().getData().add(series);
        }
        marketController.showIndexPlot();
    }

    @FXML
    private void onClearButtonClick() {
        marketController.getIndexLineChart().getData().remove(series);
    }

    @FXML
    private void onRemoveButtonClick() {
        ((StockMarket) marketController.getItem()).removeIndex(item);
        marketController.getIndexControllers().remove(this);
        marketController.getIndexLineChart().getData().remove(series);
        marketController.refresh();
        stage.close();
    }

    public synchronized void refreshPlot() {
        for (int i = series.getData().size(); i < item.getHistory().size(); i++) {
            series.getData().add(new XYChart.Data<>(item.getHistory().get(i).getTimestamp().getTime(), item.getHistory().get(i).getValue()));
        }
    }

    protected void refresh() {
        refreshPlot();
        currentValueLabel.setText(String.valueOf(item.getValue()));
        if (item instanceof MarketIndex) {
            companyListView.setItems(FXCollections.observableList(item.getCompanies()));
        }
    }

}
