package com.antsol.op.gui.market;

import com.antsol.op.asset.Asset;
import com.antsol.op.currency.Currency;
import com.antsol.op.gui.common.AbstractCreateController;
import com.antsol.op.market.*;
import com.antsol.op.settings.Settings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.util.List;

public class CreateMarketController extends AbstractCreateController<Market<? extends Asset>> {

    @FXML
    private ChoiceBox<MarketType> marketTypeChoiceBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private ChoiceBox<Currency> currencyChoiceBox;
    @FXML
    private TextField marginTextField;
    @Setter
    private List<Currency> currencies;
    @Setter
    private Settings settings;

    @Override
    protected void init() {
        marketTypeChoiceBox.setItems(FXCollections.observableList(List.of(MarketType.values())));
        currencyChoiceBox.setItems(FXCollections.observableList(currencies));
        marketTypeChoiceBox.setValue(marketTypeChoiceBox.getItems().get(0));
        nameTextField.setText(randomDataSupplier.company().name());
        countryTextField.setText(randomDataSupplier.address().country());
        cityTextField.setText(randomDataSupplier.address().city());
        addressTextField.setText(randomDataSupplier.address().streetAddress());
        currencyChoiceBox.setValue(currencyChoiceBox.getItems().get(0));
        marginTextField.setText("0.03");
    }

    @Override
    public Market<? extends Asset> create() {
        final MarketType marketType = marketTypeChoiceBox.getValue();
        return switch (marketType) {
            case STOCK -> createStockMarket();
            case CURRENCY -> createCurrencyMarket();
            case COMMODITY -> createCommodityMarket();
        };
    }

    public StockMarket createStockMarket() {
        Currency c = currencyChoiceBox.getValue();
        return new StockMarket(
                nameTextField.getText(), countryTextField.getText(),
                cityTextField.getText(), addressTextField.getText(),
                c, Float.parseFloat(marginTextField.getText()),
                settings
        );
    }

    public CurrencyMarket createCurrencyMarket() {
        Currency c = currencyChoiceBox.getValue();
        return new CurrencyMarket(
                nameTextField.getText(), countryTextField.getText(),
                cityTextField.getText(), addressTextField.getText(),
                c, Float.parseFloat(marginTextField.getText()),
                settings
        );
    }

    public CommodityMarket createCommodityMarket() {
        Currency c = currencyChoiceBox.getValue();
        return new CommodityMarket(
                nameTextField.getText(), countryTextField.getText(),
                cityTextField.getText(), addressTextField.getText(),
                c, Float.parseFloat(marginTextField.getText()),
                settings
        );
    }

}
