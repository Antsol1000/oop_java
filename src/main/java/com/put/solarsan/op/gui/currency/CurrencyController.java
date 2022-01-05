package com.put.solarsan.op.gui.currency;

import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.MainController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

public class CurrencyController extends AbstractController<Currency> {

    @Setter
    private MainController mainController;

    public static EventHandler<? super MouseEvent> onCurrencyMouseClick(final MainController mainController, final ListView<Currency> currencies) {
        return (x) -> {
            if (!currencies.getSelectionModel().isEmpty()) {
                try {
                    final Stage stage = new Stage();
                    final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/currency/currency-view.fxml"));
                    stage.setScene(new Scene(fxmlLoader.load()));
                    final CurrencyController controller = fxmlLoader.getController();
                    controller.setItem(currencies.getSelectionModel().getSelectedItem());
                    controller.setMainController(mainController);
                    controller.setStage(stage);
                    stage.show();
                } catch (Exception ignored) {}
            }
        };
    }

    @Override
    protected void init() {
        nameLabel.setText(item.getName());
        regionsLabel.setText(String.join(", ", item.getRegions()));
    }

    @FXML protected Label nameLabel;
    @FXML protected Label regionsLabel;

    @FXML protected void onRemoveButtonClick() {
        mainController.getItem().removeCurrency(item);
        mainController.refresh();
        stage.close();
    }

}
