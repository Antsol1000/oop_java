package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.Company;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateCompanyController extends CreateAssetController<Company> {

    @Override
    protected void init() {
        nameTextField.setText("apple");
        openingPriceTextField.setText("1.25");
        ipoDateTextField.setText("1999-09-01");
        ipoShareValueTextField.setText("0.76");
        capitalTextField.setText("2000000");
        profitTextField.setText("1000000");
        revenueTextField.setText("2000");
    }

    @FXML protected TextField nameTextField;
    @FXML protected TextField openingPriceTextField;
    @FXML protected TextField ipoDateTextField;
    @FXML protected TextField ipoShareValueTextField;
    @FXML protected TextField capitalTextField;
    @FXML protected TextField profitTextField;
    @FXML protected TextField revenueTextField;

    @Override
    public Company create() {
        return new Company(
                nameTextField.getText(), Float.parseFloat(openingPriceTextField.getText()),
                settings, ipoDateTextField.getText(),
                Float.parseFloat(ipoShareValueTextField.getText()),
                Float.parseFloat(capitalTextField.getText()),
                Float.parseFloat(profitTextField.getText()),
                Float.parseFloat(revenueTextField.getText())
        );
    }
}
