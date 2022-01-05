package com.antsol.op.gui.asset;

import com.antsol.op.asset.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CompanyController extends AssetController<Company> {

    @FXML
    private Label ipoDateLabel;
    @FXML
    private Label ipoShareValueLabel;
    @FXML
    private Label capitalLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private Label revenueLabel;
    @FXML
    private Label shareLimitLabel;
    @FXML
    private Label tradingVolumeLabel;
    @FXML
    private Label totalSalesLabel;
    @FXML
    private Button stopSellingButton;
    @FXML
    private Button startSellingButton;

    @Override
    protected void init() {
        super.init();
        ipoDateLabel.setText(item.getIpoDate());
        ipoShareValueLabel.setText(String.valueOf(item.getIpoShareValue()));
        capitalLabel.setText(String.valueOf(item.getCapital()));
        stopSellingButton.setOnAction(x -> onStopSellingButtonClick());
        stopSellingButton.setText("stop selling");
        startSellingButton.setOnAction(x -> onStartSellingButtonClick());
        startSellingButton.setText("start selling");
        startSellingButton.setDisable(true);
    }

    private void onStartSellingButtonClick() {
        item.hardStartSelling();
        refresh();
    }

    private void onStopSellingButtonClick() {
        item.hardStopSelling();
        refresh();
    }

    @FXML
    private void onIncreaseShareLimitButtonClick() {
        item.increaseShareLimit();
        refresh();
    }

    @FXML
    private void onBuyBackButtonClick() {
        item.hardStopSelling();
        item.buyBackAllEquities(marketController.getMainController().getItem().getFunds());
        item.buyBackAllEquities(marketController.getMainController().getItem().getInvestors());
    }

    @Override
    protected void refresh() {
        super.refresh();
        profitLabel.setText(String.valueOf(item.getProfit()));
        revenueLabel.setText(String.valueOf(item.getRevenue()));
        stopSellingButton.setDisable(!item.canStopSelling());
        startSellingButton.setDisable(!item.canStartSelling());
        totalSalesLabel.setText(String.valueOf(item.getTotalSales()));
        tradingVolumeLabel.setText(String.valueOf(item.getTradingVolume()));
        shareLimitLabel.setText(String.valueOf(item.getShareLimit()));
    }
}
