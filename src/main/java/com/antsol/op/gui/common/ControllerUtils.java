package com.antsol.op.gui.common;

import com.antsol.op.gui.MainApplication;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

@Slf4j
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

    public static LineChart<Number, Number> initializeLineChart(
            final String title, final String xTitle, final String yTitle) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        xAxis.setLabel(xTitle);
        yAxis.setForceZeroInRange(false);
        yAxis.setLabel(yTitle);
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setCreateSymbols(false);
        return lineChart;
    }

    public static Stage initializePlotStage(final LineChart<Number, Number> lineChart) {
        final StackPane stackPaneChart = new StackPane();
        stackPaneChart.getChildren().add(lineChart);

        final VBox vbox = new VBox();
        VBox.setVgrow(stackPaneChart, Priority.ALWAYS);
        vbox.getChildren().addAll(stackPaneChart);

        final Stage plotStage = new Stage();
        plotStage.setScene(new Scene(vbox, 500, 350));

        return plotStage;
    }

    public static <I, C extends AbstractCreateController<I>> void buildCreateDialog(
            final String resourcePath, final Consumer<C> beforeShow,
            final Consumer<I> itemConsumer) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourcePath));
        final DialogPane dialogPane = fxmlLoader.load();
        final C controller = fxmlLoader.getController();
        controller.setDialogPane(dialogPane);
        beforeShow.accept(controller);
        showDialog(dialogPane, simpleOkCallback(() -> itemConsumer.accept(controller.create())));
    }

    public static <I, C extends AbstractController<I>> C buildItemStage(
            final I item, final String resourcePath, final Consumer<C> beforeShow) throws IOException {
        final Stage stage = new Stage();
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourcePath));
        stage.setScene(new Scene(fxmlLoader.load()));
        final C controller = fxmlLoader.getController();
        controller.setItem(item);
        controller.setStage(stage);
        beforeShow.accept(controller);
        stage.show();
        return controller;
    }

    public static <T> Consumer<T> getEmptyConsumer() {
        return (x) -> {
        };
    }

    @SuppressWarnings("BusyWait")
    public static Thread createAndStartRefresher(final Runnable refresh, final String name, final long timestep) {
        final Thread refresher = new Thread(() -> {
            try {
                while (true) {
                    sleep(timestep);
                    Platform.runLater(refresh);
                }
            } catch (InterruptedException e) {
                log.info("{} refresher stopped", name);
            }
        });
        refresher.start();
        return refresher;
    }

}
