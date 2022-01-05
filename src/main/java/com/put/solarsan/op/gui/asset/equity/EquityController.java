package com.put.solarsan.op.gui.asset.equity;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.asset.Equity;
import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.investor.AbstractInvestorController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

public class EquityController extends AbstractController<Equity> {

    @Setter
    private AbstractInvestorController<?> investorController;

    public static EventHandler<? super MouseEvent> onEquityMouseClick(final AbstractInvestorController investorController, final ListView<Equity<? extends Asset<?>>> equities) {
        return (x) -> {
            if (!equities.getSelectionModel().isEmpty()) {
                try {
                    final Stage stage = new Stage();
                    final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/equity/equity-view.fxml"));
                    stage.setScene(new Scene(fxmlLoader.load()));
                    final EquityController controller = fxmlLoader.getController();
                    controller.setItem(equities.getSelectionModel().getSelectedItem());
                    controller.setInvestorController(investorController);
                    controller.setStage(stage);
                    stage.show();
                } catch (Exception ignored) {}
            }
        };
    }

    @Override
    protected void init() {
        nameLabel.setText(item.toString());
        originalPriceLabel.setText(String.valueOf(item.getPrice()));
        currentPriceLabel.setText(String.valueOf(calculateCurrentPrice()));
    }

    @FXML protected Label nameLabel;
    @FXML protected Label originalPriceLabel;
    @FXML protected Label currentPriceLabel;

    @FXML protected void onRefreshButtonClick() {
        currentPriceLabel.setText(String.valueOf(calculateCurrentPrice()));
    }

    @FXML protected void onSellButtonClick() {
        investorController.getItem().sellEquity(item, Float.parseFloat(currentPriceLabel.getText()));
        investorController.refresh();
        stage.close();
    }

    private Float calculateCurrentPrice() {
        return item.getValue() * item.getAsset().getCurrentPrice();
    }

}
