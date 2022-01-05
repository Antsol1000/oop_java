package com.antsol.op.gui.asset;

import com.antsol.op.asset.CurrencyAsset;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CurrencyAssetController extends AssetController<CurrencyAsset> {

    @FXML
    private Label regionsLabel;

    @Override
    protected void init() {
        super.init();
        regionsLabel.setText(String.join(", ", item.getCurrency().getRegions()));
    }
}
