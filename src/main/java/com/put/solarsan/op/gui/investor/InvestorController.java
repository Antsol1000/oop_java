package com.put.solarsan.op.gui.investor;

import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.investor.fund.participation.BuyParticipationController;
import com.put.solarsan.op.gui.investor.fund.participation.ParticipationController;
import com.put.solarsan.op.investor.PrivateInvestor;
import com.put.solarsan.op.investor.fund.InvestmentFundParticipation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;

import static com.put.solarsan.op.gui.common.ControllerUtils.showDialog;
import static com.put.solarsan.op.gui.common.ControllerUtils.simpleOkCallback;

public class InvestorController extends AbstractInvestorController<PrivateInvestor> {

    @Override
    protected void init() {
        super.init();
        firstNameLabel.setText(item.getFirstName());
        lastNameLabel.setText(item.getLastName());
        participationListView.setOnMouseClicked(ParticipationController.onParticipationMouseClick(this, participationListView));
    }

    @FXML protected Label firstNameLabel;
    @FXML protected Label lastNameLabel;
    @FXML protected ListView<InvestmentFundParticipation> participationListView;

    @FXML protected void onRemoveButtonClick() {
        mainController.getItem().removeInvestor(item);
        mainController.refresh();
        stage.close();
    }

    @FXML protected void onBuyParticipationButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class
                .getResource("/participation/buy-participation-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final BuyParticipationController controller = fxmlLoader.getController();
        controller.setDialogPane(dialogPane);
        controller.setInvestor(item);
        controller.setFunds(mainController.getItem().getFunds());
        showDialog(dialogPane, simpleOkCallback(() -> {
            item.buyParticipation(controller.create());
            refresh();
        }));
    }

}
