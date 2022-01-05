package com.antsol.op.gui.currency;

import com.antsol.op.currency.Currency;
import com.antsol.op.gui.admin.MainController;
import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.gui.common.ControllerUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

public class CurrencyController extends AbstractController<Currency> {

    @FXML
    private Label nameLabel;
    @FXML
    private Label regionsLabel;
    @Setter
    private MainController mainController;

    public static EventHandler<? super MouseEvent> onCurrencyMouseClick(final MainController mainController, final ListView<Currency> currencies) {
        return (x) -> {
            if (!currencies.getSelectionModel().isEmpty()) {
                try {
                    final Currency currency = currencies.getSelectionModel().getSelectedItem();
                    ControllerUtils.<Currency, CurrencyController>buildItemStage(
                            currency, "/currency/currency-view.fxml",
                            y -> y.setMainController(mainController));
                } catch (Exception ignored) {
                }
            }
        };
    }

    @Override
    protected void init() {
        nameLabel.setText(item.getName());
        regionsLabel.setText(String.join(", ", item.getRegions()));
    }

}
