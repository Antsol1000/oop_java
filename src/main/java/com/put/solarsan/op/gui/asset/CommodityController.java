package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.Commodity;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CommodityController extends AssetController<Commodity> {

    @FXML protected Label unitLabel;

    @Override
    protected void init() {
        super.init();
        unitLabel.setText(item.getUnit());
    }

}
