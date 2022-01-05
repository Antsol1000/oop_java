package com.antsol.op.gui.asset;

import com.antsol.op.asset.Commodity;
import com.antsol.op.market.CommodityMarket;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateCommodityController extends CreateAssetController<Commodity> {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField openingPriceTextField;
    @FXML
    private TextField unitTextField;

    @Override
    protected void init() {
        nameTextField.setText(randomDataSupplier.beer().name());
        openingPriceTextField.setText("1.25");
        unitTextField.setText("liter");
    }

    @Override
    public Commodity create() {
        return new Commodity(
                nameTextField.getText(),
                Float.parseFloat(openingPriceTextField.getText()),
                unitTextField.getText(), (CommodityMarket) market
        );
    }
}
