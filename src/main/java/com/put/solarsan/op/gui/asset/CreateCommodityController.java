package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.Commodity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateCommodityController extends CreateAssetController<Commodity> {

    @Override
    protected void init() {
        nameTextField.setText("apple");
        openingPriceTextField.setText("1.25");
        unitTextField.setText("kilogram");
    }

    @FXML protected TextField nameTextField;
    @FXML protected TextField openingPriceTextField;
    @FXML protected TextField unitTextField;

    @Override
    public Commodity create() {
        return new Commodity(
                nameTextField.getText(),
                Float.parseFloat(openingPriceTextField.getText()),
                settings, unitTextField.getText()
        );
    }
}
