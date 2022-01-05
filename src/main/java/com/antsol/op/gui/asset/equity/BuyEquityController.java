package com.antsol.op.gui.asset.equity;

import com.antsol.op.asset.Asset;
import com.antsol.op.asset.Company;
import com.antsol.op.gui.common.AbstractBuyController;
import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.investor.product.BuyableWrapper;
import com.antsol.op.market.Market;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class BuyEquityController extends AbstractBuyController<BuyableWrapper<Asset>, AbstractInvestor> {

    @FXML
    private ChoiceBox<Market<Asset>> marketChoiceBox;
    @FXML
    private Label assetLabel;
    @FXML
    private ChoiceBox<Asset> assetChoiceBox;
    @Setter
    private List<Market<Asset>> markets;

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

    private void refreshAssets() {
        Market<Asset> market = marketChoiceBox.getValue();
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
        Asset asset = assetChoiceBox.getValue();
        if (asset instanceof Company) {
            valueTextField.setText(String.valueOf((int) Float.parseFloat(valueTextField.getText())));
        }
        priceLabel.setText(String.valueOf(asset.getCurrentBuyPrice(Float.parseFloat(valueTextField.getText()))));
        dialogPane.lookupButton(ButtonType.OK).setDisable(false);
    }

    @Override
    public BuyableWrapper<Asset> create() {
        return new BuyableWrapper<>(assetChoiceBox.getValue(),
                Float.parseFloat(valueTextField.getText()),
                Float.parseFloat(priceLabel.getText()));
    }

}
