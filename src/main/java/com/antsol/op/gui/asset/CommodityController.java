package com.antsol.op.gui.asset;

import com.antsol.op.asset.Commodity;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CommodityController extends AssetController<Commodity> {

    @FXML
    private Label unitLabel;

    @Override
    protected void init() {
        super.init();
        unitLabel.setText(item.getUnit());
    }

}
