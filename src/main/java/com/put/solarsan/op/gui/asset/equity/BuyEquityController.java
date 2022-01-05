package com.put.solarsan.op.gui.asset.equity;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.asset.Equity;
import com.put.solarsan.op.gui.common.AbstractBuyController;
import com.put.solarsan.op.investor.AbstractInvestor;
import com.put.solarsan.op.market.Market;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.List;
import java.util.stream.Collectors;

public class BuyEquityController extends AbstractBuyController<Equity, AbstractInvestor> {

    private List<Market<?>> markets;

    @Override
    protected void init() {
        super.init();
        marketChoiceBox.setItems(FXCollections.observableList(
                markets.stream()
                        .filter(x -> x.getCurrency().equals(investor.getCurrency()))
                        .collect(Collectors.toList())));
        marketChoiceBox.setOnAction((x) -> refreshAssets());
        assetChoiceBox.setOnAction((x) -> enablePrice());
        assetChoiceBox.setDisable(true);
    }

    @FXML protected ChoiceBox<Market<?>> marketChoiceBox;
    @FXML protected Label assetLabel;
    @FXML protected ChoiceBox<Asset<?>> assetChoiceBox;

    private void refreshAssets() {
        Market market = marketChoiceBox.getValue();
        assetLabel.setText(switch (market.getType()) {
            case STOCK -> "company";
            case CURRENCY -> "currency";
            case COMMODITY -> "commodity";
        });
        assetChoiceBox.setDisable(false);
        assetChoiceBox.getSelectionModel().clearSelection();
        assetChoiceBox.setItems(FXCollections.observableList(market.getAssetsList()));
    }

    protected void refreshPrice() {
        Market<?> market = marketChoiceBox.getValue();
        Asset<?> asset = assetChoiceBox.getValue();
        priceLabel.setText(String.valueOf(
                Float.parseFloat(valueTextField.getText()) *
                        asset.getCurrentPrice() * (1 + market.getMargin())
        ));
        dialogPane.lookupButton(ButtonType.OK).setDisable(false);
    }

    @Override
    public Equity create() {
        return new Equity(assetChoiceBox.getValue(),
                Float.parseFloat(valueTextField.getText()),
                Float.parseFloat(priceLabel.getText()));
    }

    public void setMarkets(List<Market<?>> markets) {
        this.markets = markets;
    }

}
