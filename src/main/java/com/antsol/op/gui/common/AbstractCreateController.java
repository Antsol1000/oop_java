package com.antsol.op.gui.common;

import com.github.javafaker.Faker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import lombok.Setter;

public abstract class AbstractCreateController<I> {

    protected static final Faker randomDataSupplier = new Faker();

    @Setter
    protected DialogPane dialogPane;

    protected abstract void init();

    public abstract I create();

    @FXML
    private void initialize() {
        Platform.runLater(this::init);
    }

}
