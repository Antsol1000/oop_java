package com.put.solarsan.op.gui.investor.fund;

import com.put.solarsan.op.gui.investor.AbstractInvestorController;
import com.put.solarsan.op.investor.fund.InvestmentFund;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FundController extends AbstractInvestorController<InvestmentFund> {

    @Override
    protected void init() {
        super.init();
        nameLabel.setText(item.getName());
        managerLabel.setText(item.getManager());
    }

    @FXML protected Label nameLabel;
    @FXML protected Label managerLabel;

    @FXML protected void onRemoveButtonClick() {
        mainController.getItem().removeFund(item);
        mainController.refresh();
        stage.close();
    }

}
