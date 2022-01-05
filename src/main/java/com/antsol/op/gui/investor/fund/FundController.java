package com.antsol.op.gui.investor.fund;

import com.antsol.op.gui.investor.AbstractInvestorController;
import com.antsol.op.investor.fund.InvestmentFund;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FundController extends AbstractInvestorController<InvestmentFund> {

    @FXML
    private Label nameLabel;
    @FXML
    private Label managerLabel;

    @Override
    protected void init() {
        super.init();
        nameLabel.setText(item.getFundName());
        managerLabel.setText(item.getManager());
    }

    @FXML
    private void onRemoveButtonClick() {
        mainController.getItem().removeFund(item);
        mainController.refresh();
        stage.close();
    }

}
