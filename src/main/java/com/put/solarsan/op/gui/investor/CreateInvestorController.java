package com.put.solarsan.op.gui.investor;

import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.investor.PrivateInvestor;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateInvestorController extends AbstractCreateInvestorController<PrivateInvestor> {

    @Override
    protected void init() {
        super.init();
        firstNameTextField.setText("Jordan");
        lastNameTextField.setText("Belford");
    }

    @FXML protected TextField firstNameTextField;
    @FXML protected TextField lastNameTextField;

    public PrivateInvestor create() {
        Currency c = currencyChoiceBox.getValue();
        return new PrivateInvestor(
                Float.parseFloat(budgetTextField.getText()), c,
                firstNameTextField.getText(), lastNameTextField.getText()
        );
    }

}
