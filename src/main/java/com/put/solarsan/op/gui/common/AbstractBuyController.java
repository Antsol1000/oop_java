package com.put.solarsan.op.gui.common;

import com.put.solarsan.op.investor.AbstractInvestor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Setter;

public abstract class AbstractBuyController<B, I extends AbstractInvestor> extends AbstractCreateController<B> {

    @Setter
    protected I investor;

    @Override
    protected void init() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        budgetLabel.setText(String.valueOf(investor.getBudget()));
        refreshButton.setOnAction((x) -> refreshPrice());
        valueTextField.setDisable(true);
        refreshButton.setDisable(true);
        valueTextField.setText("0.0");
        priceLabel.setText("0.0");
    }

    @FXML protected Label budgetLabel;
    @FXML protected Label priceLabel;
    @FXML protected TextField valueTextField;
    @FXML protected Button refreshButton;

    protected abstract void refreshPrice();

    protected void enablePrice() {
        refreshButton.setDisable(false);
        valueTextField.setDisable(false);
    }

    public float getPrice() {
        return Float.parseFloat(priceLabel.getText());
    }

}
