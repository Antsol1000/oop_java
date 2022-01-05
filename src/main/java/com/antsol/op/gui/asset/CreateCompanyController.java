package com.antsol.op.gui.asset;

import com.antsol.op.asset.Company;
import com.antsol.op.market.StockMarket;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateCompanyController extends CreateAssetController<Company> {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField openingPriceTextField;
    @FXML
    private TextField ipoDateTextField;
    @FXML
    private TextField ipoShareValueTextField;
    @FXML
    private TextField shareLimitTextField;
    @FXML
    private TextField capitalTextField;
    @FXML
    private TextField profitTextField;
    @FXML
    private TextField revenueTextField;

    @Override
    protected void init() {
        nameTextField.setText(randomDataSupplier.company().name());
        openingPriceTextField.setText("1.25");
        ipoDateTextField.setText(randomDataSupplier.date().birthday().toString());
        ipoShareValueTextField.setText("0.76");
        shareLimitTextField.setText("50000");
        capitalTextField.setText("2000000");
        profitTextField.setText("1000000");
        revenueTextField.setText("2000");
    }

    @Override
    public Company create() {
        return new Company(
                nameTextField.getText(), Float.parseFloat(openingPriceTextField.getText()),
                ipoDateTextField.getText(),
                Float.parseFloat(ipoShareValueTextField.getText()),
                Long.parseLong(shareLimitTextField.getText()),
                Float.parseFloat(capitalTextField.getText()),
                Float.parseFloat(profitTextField.getText()),
                Float.parseFloat(revenueTextField.getText()),
                (StockMarket) market
        );
    }
}
