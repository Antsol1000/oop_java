package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.CurrencyAsset;
import com.put.solarsan.op.currency.Currency;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class CreateCurrencyAssetController extends CreateAssetController<CurrencyAsset> {

    protected List<Currency> currencies;

    @Override
    protected void init() {
        currencyChoiceBox.setItems(FXCollections.observableList(currencies));
        openingPriceTextField.setText("1.25");
    }

    @FXML protected TextField openingPriceTextField;
    @FXML protected ChoiceBox<Currency> currencyChoiceBox;

    @Override
    public CurrencyAsset create() {
        return new CurrencyAsset(
                Float.parseFloat(openingPriceTextField.getText()),
                currencyChoiceBox.getValue(),
                settings
        );
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

}
