package com.antsol.op.gui.investor;

import com.antsol.op.asset.Asset;
import com.antsol.op.currency.Currency;
import com.antsol.op.gui.common.AbstractCreateController;
import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.market.Market;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.util.List;

public abstract class AbstractCreateInvestorController<I extends AbstractInvestor> extends AbstractCreateController<I> {

    @Setter
    private List<Currency> currencies;

    @Setter
    protected List<Market<? extends Asset>> markets;

    @FXML
    protected TextField budgetTextField;
    @FXML
    protected ChoiceBox<Currency> currencyChoiceBox;

    @Override
    protected void init() {
        currencyChoiceBox.setItems(FXCollections.observableList(currencies));
        budgetTextField.setText("1000");
        currencyChoiceBox.setValue(currencyChoiceBox.getItems().get(0));
    }

}
