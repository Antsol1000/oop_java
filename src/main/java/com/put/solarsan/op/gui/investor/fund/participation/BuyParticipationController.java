package com.put.solarsan.op.gui.investor.fund.participation;

import com.put.solarsan.op.gui.common.AbstractBuyController;
import com.put.solarsan.op.investor.PrivateInvestor;
import com.put.solarsan.op.investor.fund.InvestmentFund;
import com.put.solarsan.op.investor.fund.InvestmentFundParticipation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

public class BuyParticipationController extends AbstractBuyController<InvestmentFundParticipation, PrivateInvestor> {

    private List<InvestmentFund> funds;

    @Override
    protected void init() {
        super.init();
        fundChoiceBox.setItems(FXCollections.observableList(
                funds.stream()
                        .filter(x -> x.getCurrency().equals(investor.getCurrency()))
                        .collect(Collectors.toList())));
        fundChoiceBox.setOnAction((x) -> enablePrice());
    }

    @FXML protected ChoiceBox<InvestmentFund> fundChoiceBox;

    protected void refreshPrice() {
        final InvestmentFund fund = fundChoiceBox.getValue();
        priceLabel.setText(String.valueOf(
                Float.parseFloat(valueTextField.getText()) * fund.getEquitiesValue())
        );
        dialogPane.lookupButton(ButtonType.OK).setDisable(false);
    }

    @Override
    public InvestmentFundParticipation create() {
        return new InvestmentFundParticipation(fundChoiceBox.getValue(),
                Float.parseFloat(valueTextField.getText()),
                Float.parseFloat(priceLabel.getText()));
    }

    public void setFunds(List<InvestmentFund> funds) {
        this.funds = funds;
    }
}
