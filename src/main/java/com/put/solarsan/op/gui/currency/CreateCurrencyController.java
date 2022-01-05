package com.put.solarsan.op.gui.currency;

import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.gui.common.AbstractCreateController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class CreateCurrencyController extends AbstractCreateController<Currency> {

    @FXML protected TextField nameTextField;
    @FXML protected TextField regionsTextField;

    @Override
    protected void init() {
        nameTextField.setText("euro");
        regionsTextField.setText("Germany, France");
    }

    @Override
    public Currency create() {
        return new Currency(nameTextField.getText(), List.of(regionsTextField.getText().split(",")));
    }

}
