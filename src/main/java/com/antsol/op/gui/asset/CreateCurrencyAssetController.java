package com.antsol.op.gui.asset;

import com.antsol.op.asset.CurrencyAsset;
import com.antsol.op.currency.Currency;
import com.antsol.op.market.CurrencyMarket;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.util.List;

public class CreateCurrencyAssetController extends CreateAssetController<CurrencyAsset> {

    @Setter
    private List<Currency> currencies;
    @FXML
    private TextField openingPriceTextField;
    @FXML
    private ChoiceBox<Currency> currencyChoiceBox;

    @Override
    protected void init() {
        currencyChoiceBox.setItems(FXCollections.observableList(currencies));
        openingPriceTextField.setText("1.25");
    }

    @Override
    public CurrencyAsset create() {
        return new CurrencyAsset(
                Float.parseFloat(openingPriceTextField.getText()),
                currencyChoiceBox.getValue(),
                (CurrencyMarket) market
        );
    }

}
