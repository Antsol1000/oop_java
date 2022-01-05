package com.put.solarsan.op.gui.common;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import lombok.Setter;

public abstract class AbstractCreateController<I> {

    @Setter
    protected DialogPane dialogPane;

    protected abstract void init();

    public abstract I create();

    @FXML
    private void initialize() {
        Platform.runLater(this::init);
    }

}
