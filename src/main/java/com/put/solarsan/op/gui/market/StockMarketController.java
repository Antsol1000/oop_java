package com.put.solarsan.op.gui.market;

import com.put.solarsan.op.asset.Company;
import com.put.solarsan.op.gui.common.ControllerUtils;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.market.index.CreateIndexController;
import com.put.solarsan.op.gui.market.index.IndexController;
import com.put.solarsan.op.market.StockMarket;
import com.put.solarsan.op.market.index.Index;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.put.solarsan.op.gui.common.ControllerUtils.showDialog;
import static com.put.solarsan.op.gui.common.ControllerUtils.simpleOkCallback;

public class StockMarketController extends MarketController<Company> {

    protected Stage indexPlotStage;
    @Getter
    protected LineChart<Number, Number> indexLineChart;
    @Getter
    protected List<IndexController> indexControllers;

    @Override
    protected void init() {
        super.init();
        indexListView.setOnMouseClicked(IndexController.onIndexMouseClick(this, indexListView));
        indexControllers = new ArrayList<>();
        indexLineChart = ControllerUtils.initializeLineChart("index price");
        indexPlotStage = ControllerUtils.initializePlotStage(indexLineChart, this::refreshIndexPlot);
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

    @FXML protected ListView<Index> indexListView;

    @FXML protected void onCreateIndexButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/index/create-index-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final CreateIndexController controller = fxmlLoader.getController();
        controller.setMarket((StockMarket) item);
        controller.setCompanies(item.getAssetsList());
        controller.setDialogPane(dialogPane);
        showDialog(dialogPane, simpleOkCallback(() -> {
            ((StockMarket) item).createIndex(controller.create());
            refresh();
        }));
    }

}
