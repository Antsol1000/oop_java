package com.put.solarsan.op.gui.market;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.gui.common.AbstractCreateController;
import com.put.solarsan.op.market.*;
import com.put.solarsan.op.settings.Settings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class CreateMarketController extends AbstractCreateController<Market<? extends Asset<?>>> {

    private List<Currency> currencies;
    private Settings settings;

    @FXML protected ChoiceBox<MarketType> marketTypeChoiceBox;
    @FXML protected TextField nameTextField;
    @FXML protected TextField countryTextField;
    @FXML protected TextField cityTextField;
    @FXML protected TextField addressTextField;
    @FXML protected ChoiceBox<Currency> currencyChoiceBox;
    @FXML protected TextField marginTextField;

    @Override
    protected void init() {
        marketTypeChoiceBox.setItems(FXCollections.observableList(List.of(MarketType.values())));
        currencyChoiceBox.setItems(FXCollections.observableList(currencies));
        marketTypeChoiceBox.setValue(marketTypeChoiceBox.getItems().get(0));
        nameTextField.setText("gielda");
        countryTextField.setText("Germany");
        cityTextField.setText("Berlin");
        addressTextField.setText("HermanStrasse 12");
        currencyChoiceBox.setValue(currencyChoiceBox.getItems().get(0));
        marginTextField.setText("0.03");
    }

    @Override
    public Market<?> create() {
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

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public void setCurrencies(final List<Currency> currencies) {
            this.currencies = currencies;
        }
    }
