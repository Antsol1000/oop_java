package com.antsol.op.gui.investor.fund.participation;

import com.antsol.op.gui.common.AbstractBuyController;
import com.antsol.op.investor.investor.PrivateInvestor;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.investor.product.BuyableWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class BuyParticipationController
        extends AbstractBuyController<BuyableWrapper<InvestmentFund>, PrivateInvestor> {

    @FXML
    private ChoiceBox<InvestmentFund> fundChoiceBox;
    @Setter
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

    protected void refreshPrice() {
        final InvestmentFund fund = fundChoiceBox.getValue();
        priceLabel.setText(String.valueOf(fund.getCurrentBuyPrice(Float.parseFloat(valueTextField.getText()))));
        dialogPane.lookupButton(ButtonType.OK).setDisable(false);
    }

    @Override
    public BuyableWrapper<InvestmentFund> create() {
        return new BuyableWrapper<>(fundChoiceBox.getValue(),
                Float.parseFloat(valueTextField.getText()),
                Float.parseFloat(priceLabel.getText()));
    }

}
