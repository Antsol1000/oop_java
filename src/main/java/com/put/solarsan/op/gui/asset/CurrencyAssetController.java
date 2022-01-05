package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.CurrencyAsset;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CurrencyAssetController extends AssetController<CurrencyAsset> {

    @FXML protected Label regionsLabel;

    @Override
    protected void init() {
        super.init();
        regionsLabel.setText(String.join(", ", item.getCurrency().getRegions()));
    }
}
