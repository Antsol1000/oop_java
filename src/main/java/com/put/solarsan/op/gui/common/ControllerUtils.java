package com.put.solarsan.op.gui.common;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ControllerUtils {

    public static <R> Callback<ButtonType, R> simpleOkCallback(Runnable function) {
        return (b) -> {
            if (b == ButtonType.OK) {
                function.run();
            }
            return null;
        };
    }

    public static <R> void showDialog(final DialogPane dialogPane, Callback<ButtonType, R> callback) {
        final Dialog<R> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setResultConverter(callback);
        dialog.showAndWait();
    }

    public static LineChart<Number, Number> initializeLineChart(final String title) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        xAxis.setLabel("time");
        yAxis.setForceZeroInRange(false);
        yAxis.setLabel("price");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("PLOT");
        lineChart.setCreateSymbols(false);
        return lineChart;
    }

    public static Stage initializePlotStage(final LineChart<Number, Number> lineChart, final Runnable refreshAction) {
        final StackPane stackPaneChart = new StackPane();
        stackPaneChart.getChildren().add(lineChart);

        final Button refreshButton = new Button("refresh");
        refreshButton.setOnMouseClicked((x) -> refreshAction.run());

        final StackPane stackPaneButton = new StackPane();
        stackPaneButton.getChildren().add(refreshButton);

        final VBox vbox = new VBox();
        VBox.setVgrow(stackPaneChart, Priority.ALWAYS);
        vbox.getChildren().addAll(stackPaneChart, stackPaneButton);

        final Stage plotStage = new Stage();
        plotStage.setScene(new Scene(vbox,500,350));

        return plotStage;
    }

}
