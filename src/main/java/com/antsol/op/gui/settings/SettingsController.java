package com.antsol.op.gui.settings;

import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.settings.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsController extends AbstractController<Settings> {

    @FXML
    private TextField timeStepTextField;
    @FXML
    private TextField changeIndexTextField;

    @Override
    protected void init() {
        timeStepTextField.setText(String.valueOf(item.getTimeStep()));
        changeIndexTextField.setText(String.valueOf(item.getChangeIndex()));
    }

    public void setSettings() {
        item.setTimeStep(Integer.parseInt(timeStepTextField.getText()));
        item.setChangeIndex(Float.parseFloat(changeIndexTextField.getText()));
    }
}
