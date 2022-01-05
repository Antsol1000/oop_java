package com.antsol.op.gui;

import com.antsol.op.admin.Admin;
import com.antsol.op.gui.admin.MainController;
import com.antsol.op.gui.common.ControllerUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    public static void main() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        final Admin admin = new Admin();
        admin.start();
        ControllerUtils.<Admin, MainController>buildItemStage(
                admin, "/main-view.fxml",
                ControllerUtils.getEmptyConsumer()
        );
    }
}