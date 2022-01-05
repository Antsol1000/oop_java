package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CompanyController extends AssetController<Company> {

    @FXML protected Label ipoDateLabel;
    @FXML protected Label ipoShareValueLabel;
    @FXML protected Label capitalLabel;
    @FXML protected Label profitLabel;
    @FXML protected Label revenueLabel;

    @Override
    protected void init() {
        super.init();
        ipoDateLabel.setText(item.getIpoDate());
        ipoShareValueLabel.setText(String.valueOf(item.getIpoShareValue()));
        capitalLabel.setText(String.valueOf(item.getCapital()));
        profitLabel.setText(String.valueOf(item.getProfit()));
        revenueLabel.setText(String.valueOf(item.getRevenue()));
    }
}
