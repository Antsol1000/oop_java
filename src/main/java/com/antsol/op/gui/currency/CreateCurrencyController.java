package com.antsol.op.gui.currency;

import com.antsol.op.currency.Currency;
import com.antsol.op.gui.common.AbstractCreateController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class CreateCurrencyController extends AbstractCreateController<Currency> {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField regionsTextField;

    @Override
    protected void init() {
        nameTextField.setText(randomDataSupplier.currency().name());
        regionsTextField.setText(randomDataSupplier.address().country());
    }

    @Override
    public Currency create() {
        return new Currency(nameTextField.getText(), List.of(regionsTextField.getText().split(",")));
    }

}
