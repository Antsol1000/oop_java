package com.put.solarsan.op.gui.settings;

import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.settings.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsController extends AbstractController<Settings> {

    @Override
    protected void init() {
        timeStepTextField.setText(String.valueOf(item.getTimeStep()));
        changeIndexTextField.setText(String.valueOf(item.getChangeIndex()));
    }

    @FXML protected TextField timeStepTextField;
    @FXML protected TextField changeIndexTextField;

    public void setSettings() {
        item.setTimeStep(Integer.parseInt(timeStepTextField.getText()));
        item.setChangeIndex(Float.parseFloat(changeIndexTextField.getText()));
    }
}
