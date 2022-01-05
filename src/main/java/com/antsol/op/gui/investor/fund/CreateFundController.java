package com.antsol.op.gui.investor.fund;

import com.antsol.op.currency.Currency;
import com.antsol.op.gui.investor.AbstractCreateInvestorController;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.settings.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Setter;

public class CreateFundController extends AbstractCreateInvestorController<InvestmentFund> {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField managerTextField;
    @FXML
    private TextField marginTextField;
    @Setter
    private Settings settings;

    @Override
    protected void init() {
        super.init();
        nameTextField.setText(randomDataSupplier.company().name());
        managerTextField.setText(randomDataSupplier.witcher().character());
        marginTextField.setText("0.05");
    }

    @Override
    public InvestmentFund create() {
        Currency c = currencyChoiceBox.getValue();
        return new InvestmentFund(
                Float.parseFloat(budgetTextField.getText()), c,
                nameTextField.getText(), managerTextField.getText(),
                Float.parseFloat(marginTextField.getText()), settings, markets
        );
    }

}
