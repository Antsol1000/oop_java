package com.antsol.op.gui.common;

import com.antsol.op.investor.AbstractInvestor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Setter;

public abstract class AbstractBuyController<B, I extends AbstractInvestor> extends AbstractCreateController<B> {

    @Setter
    protected I investor;
    @FXML
    private Label budgetLabel;
    @FXML
    protected Label priceLabel;
    @FXML
    protected TextField valueTextField;
    @FXML
    private Button refreshButton;

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

    protected abstract void refreshPrice();

    protected void enablePrice() {
        refreshButton.setDisable(false);
        valueTextField.setDisable(false);
    }

}
