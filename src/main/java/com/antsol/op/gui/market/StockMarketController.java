package com.antsol.op.gui.market;

import com.antsol.op.asset.Company;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.gui.market.index.CreateIndexController;
import com.antsol.op.gui.market.index.IndexController;
import com.antsol.op.market.StockMarket;
import com.antsol.op.market.index.Index;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockMarketController extends MarketController<Company> {

    private Stage indexPlotStage;
    @Getter
    private LineChart<Number, Number> indexLineChart;
    @Getter
    private List<IndexController> indexControllers;
    @FXML
    protected ListView<Index> indexListView;

    @Override
    protected void init() {
        super.init();
        indexListView.setOnMouseClicked(IndexController.onIndexMouseClick(this, indexListView));
        indexControllers = new ArrayList<>();
        indexLineChart = ControllerUtils.initializeLineChart("index price", "time", "price");
        indexPlotStage = ControllerUtils.initializePlotStage(indexLineChart);
    }

    @Override
    public void refresh() {
        super.refresh();
        indexListView.setItems(FXCollections.observableList(((StockMarket) item).getIndexes()));
    }

    public void showIndexPlot() {
        if (!indexPlotStage.isShowing()) {
            indexPlotStage.show();
        }
    }

    public void refreshIndexPlot() {
        this.indexControllers.forEach(IndexController::refreshPlot);
    }

    @FXML
    private void onCreateIndexButtonClick() throws IOException {
        ControllerUtils.<Index, CreateIndexController>buildCreateDialog(
                "/index/create-index-view.fxml",
                x -> {
                    x.setMarket((StockMarket) item);
                    x.setCompanies(item.getAssetsList());
                },
                x -> {
                    ((StockMarket) item).createIndex(x);
                    refresh();
                });
    }

}
