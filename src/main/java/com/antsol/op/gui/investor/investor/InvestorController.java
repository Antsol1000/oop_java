package com.antsol.op.gui.investor.investor;

import com.antsol.op.gui.investor.fund.participation.BuyParticipationController;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.gui.investor.AbstractInvestorController;
import com.antsol.op.investor.investor.PrivateInvestor;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.investor.product.BuyableWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class InvestorController extends AbstractInvestorController<PrivateInvestor> {

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;

    @Override
    protected void init() {
        super.init();
        firstNameLabel.setText(item.getFirstName());
        lastNameLabel.setText(item.getLastName());
    }

    @FXML
    private void onRemoveButtonClick() {
        mainController.getItem().removeInvestor(item);
        mainController.refresh();
        stage.close();
    }

    @FXML
    private void onBuyParticipationButtonClick() throws IOException {
        ControllerUtils.<BuyableWrapper<InvestmentFund>, BuyParticipationController>buildCreateDialog(
                "/participation/buy-participation-view.fxml",
                x -> {
                    x.setInvestor(item);
                    x.setFunds(mainController.getItem().getFunds());
                },
                x -> {
                    item.buy(x);
                    refresh();
                });
    }

}
