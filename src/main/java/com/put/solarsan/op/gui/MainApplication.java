package com.put.solarsan.op.gui;

import com.put.solarsan.op.admin.Admin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Admin admin = new Admin();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainController controller = fxmlLoader.getController();
        controller.setItem(admin);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}