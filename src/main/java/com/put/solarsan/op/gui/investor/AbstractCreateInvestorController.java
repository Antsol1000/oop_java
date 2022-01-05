package com.put.solarsan.op.gui.investor;

import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.gui.common.AbstractCreateController;
import com.put.solarsan.op.investor.AbstractInvestor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.util.List;

public abstract class AbstractCreateInvestorController<I extends AbstractInvestor> extends AbstractCreateController<I> {

    @Setter
    protected List<Currency> currencies;

    @FXML protected TextField budgetTextField;
    @FXML protected ChoiceBox<Currency> currencyChoiceBox;

    @Override
    protected void init() {
        currencyChoiceBox.setItems(FXCollections.observableList(currencies));
        budgetTextField.setText("1000");
        currencyChoiceBox.setValue(currencyChoiceBox.getItems().get(0));
    }

}
