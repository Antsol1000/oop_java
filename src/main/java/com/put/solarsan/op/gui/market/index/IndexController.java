package com.put.solarsan.op.gui.market.index;

import com.put.solarsan.op.asset.Company;
import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.market.StockMarketController;
import com.put.solarsan.op.market.StockMarket;
import com.put.solarsan.op.market.index.Index;
import com.put.solarsan.op.market.index.MarketIndex;
import javafx.collections.FXCollections;
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

public class IndexController extends AbstractController<Index> {

    @Setter
    protected StockMarketController marketController;

    protected XYChart.Series<Number, Number> series;
    protected int it;

    public static EventHandler<? super MouseEvent> onIndexMouseClick(final StockMarketController marketController, final ListView<Index> indexes) {
        return (x) -> {
            if (!indexes.getSelectionModel().isEmpty()) {
                try {
                    final Stage stage = new Stage();
                    final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/index/index-view.fxml"));
                    stage.setScene(new Scene(fxmlLoader.load()));
                    final IndexController controller = fxmlLoader.getController();
                    controller.setMarketController(marketController);
                    controller.setItem(indexes.getSelectionModel().getSelectedItem());
                    controller.setStage(stage);
                    marketController.getIndexControllers().add(controller);
                    stage.show();
                } catch (Exception ignored) {}
            }
        };
    }

    @Override
    protected void init() {
        series = new XYChart.Series<>();
        marketController.getIndexLineChart().getData().add(series);
        it = 0;
        nameLabel.setText(item.toString());
        companyListView.setItems(FXCollections.observableList(item.getCompanies()));
        refresh();
    }

    @FXML protected Label nameLabel;
    @FXML protected Label currentValueLabel;
    @FXML protected ListView<Company> companyListView;

    @FXML protected void onPlotButtonClick() {
        refreshPlot();
        series.getNode().setVisible(true);
        marketController.showIndexPlot();
    }

    @FXML protected void onClearButtonClick() {
        series.getNode().setVisible(false);
    }

    @FXML protected void onRefreshButtonClick() {
        refreshPlot();
        refresh();
    }

    @FXML protected void onRemoveButtonClick() {
        ((StockMarket) marketController.getItem()).removeIndex(item);
        marketController.getIndexControllers().remove(this);
        marketController.getIndexLineChart().getData().remove(series);
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
        currentValueLabel.setText(String.valueOf(item.getValue()));
        if (item instanceof MarketIndex) {
            companyListView.setItems(FXCollections.observableList(item.getCompanies()));
        }
    }

}
