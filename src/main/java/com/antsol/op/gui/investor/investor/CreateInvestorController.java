package com.antsol.op.gui.investor.investor;

import com.antsol.op.currency.Currency;
import com.antsol.op.gui.investor.AbstractCreateInvestorController;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.investor.investor.PrivateInvestor;
import com.antsol.op.settings.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.util.List;

public class CreateInvestorController extends AbstractCreateInvestorController<PrivateInvestor> {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @Setter
    private List<InvestmentFund> funds;
    @Setter
    private Settings settings;

    @Override
    protected void init() {
        super.init();
        firstNameTextField.setText(randomDataSupplier.name().firstName());
        lastNameTextField.setText(randomDataSupplier.name().lastName());
    }

    public PrivateInvestor create() {
        Currency c = currencyChoiceBox.getValue();
        return new PrivateInvestor(
                Float.parseFloat(budgetTextField.getText()), c,
                firstNameTextField.getText(), lastNameTextField.getText(),
                settings, markets, funds
        );
    }

}
