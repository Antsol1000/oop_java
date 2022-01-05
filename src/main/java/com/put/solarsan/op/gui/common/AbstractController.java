package com.put.solarsan.op.gui.common;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractController<I> {

    @Setter
    protected Stage stage;

    @Setter
    @Getter
    protected I item;

    protected abstract void init();

    @FXML private void initialize() {
        Platform.runLater(this::init);
    }

}
