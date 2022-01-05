package com.put.solarsan.op.gui.investor.fund;

import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.gui.investor.AbstractCreateInvestorController;
import com.put.solarsan.op.investor.fund.InvestmentFund;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateFundController extends AbstractCreateInvestorController<InvestmentFund> {

    @Override
    protected void init() {
        super.init();
        nameTextField.setText("bocian");
        managerTextField.setText("Janusz Bociek");
    }

    @FXML protected TextField nameTextField;
    @FXML protected TextField managerTextField;

    @Override
    public InvestmentFund create() {
        Currency c = currencyChoiceBox.getValue();
        return new InvestmentFund(
                Float.parseFloat(budgetTextField.getText()), c,
                nameTextField.getText(), managerTextField.getText()
        );
    }

}
