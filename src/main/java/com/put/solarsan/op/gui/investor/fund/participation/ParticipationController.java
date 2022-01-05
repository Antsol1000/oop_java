package com.put.solarsan.op.gui.investor.fund.participation;

import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.investor.InvestorController;
import com.put.solarsan.op.investor.fund.InvestmentFundParticipation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

public class ParticipationController extends AbstractController<InvestmentFundParticipation> {

    @Setter
    private InvestorController investorController;

    public static EventHandler<? super MouseEvent> onParticipationMouseClick(final InvestorController investorController, final ListView<InvestmentFundParticipation> participations) {
        return (x) -> {
            if (!participations.getSelectionModel().isEmpty()) {
                try {
                    final Stage stage = new Stage();
                    final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/equity/equity-view.fxml"));
                    stage.setScene(new Scene(fxmlLoader.load()));
                    final ParticipationController controller = fxmlLoader.getController();
                    controller.setItem(participations.getSelectionModel().getSelectedItem());
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
        currentPriceLabel.setText(String.valueOf(item.getValue()));
    }

    @FXML protected Label nameLabel;
    @FXML protected Label originalPriceLabel;
    @FXML protected Label currentPriceLabel;

    @FXML protected void onRefreshButtonClick() {
        currentPriceLabel.setText(String.valueOf(item.getValue()));
    }

    @FXML protected void onSellButtonClick() {
        investorController.getItem().sellParticipation(item, Float.parseFloat(currentPriceLabel.getText()));
        investorController.refresh();
        stage.close();
    }

}
