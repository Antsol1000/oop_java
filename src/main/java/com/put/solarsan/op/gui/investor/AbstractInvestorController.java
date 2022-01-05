package com.put.solarsan.op.gui.investor;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.asset.Equity;
import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.MainApplication;
import com.put.solarsan.op.gui.MainController;
import com.put.solarsan.op.gui.asset.equity.BuyEquityController;
import com.put.solarsan.op.gui.asset.equity.EquityController;
import com.put.solarsan.op.investor.AbstractInvestor;
import com.put.solarsan.op.investor.PrivateInvestor;
import com.put.solarsan.op.investor.fund.InvestmentFund;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

import static com.put.solarsan.op.gui.common.ControllerUtils.showDialog;
import static com.put.solarsan.op.gui.common.ControllerUtils.simpleOkCallback;

public abstract class AbstractInvestorController<I extends AbstractInvestor> extends AbstractController<I> {

    @Setter
    protected MainController mainController;

    public static <I extends AbstractInvestor> EventHandler<? super MouseEvent> onInvestorMouseClick(final MainController mainController, final ListView<I> investors) {
        return (x) -> {
            if (!investors.getSelectionModel().isEmpty()) {
                try {
                    String resourcePath = "/%s/%s-view.fxml";
                    if (investors.getSelectionModel().getSelectedItem() instanceof InvestmentFund) {
                        resourcePath = String.format(resourcePath, "fund", "fund");
                    } else if (investors.getSelectionModel().getSelectedItem() instanceof PrivateInvestor) {
                        resourcePath = String.format(resourcePath, "investor", "investor");
                    }
                    final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resourcePath));
                    final Stage stage = new Stage();
                    stage.setScene(new Scene(fxmlLoader.load()));
                    final AbstractInvestorController<I> controller = fxmlLoader.getController();
                    controller.setItem(investors.getSelectionModel().getSelectedItem());
                    controller.setMainController(mainController);
                    controller.setStage(stage);
                    stage.show();
                } catch (Exception ignored) {}
            }
        };
    }

    @Override
    protected void init() {
        currencyLabel.setText(item.getCurrency().toString());
        equityListView.setOnMouseClicked(EquityController.onEquityMouseClick(this, equityListView));
        refresh();
    }

    @FXML protected Label budgetLabel;
    @FXML protected Label currencyLabel;
    @FXML protected ListView<Equity<? extends Asset<?>>> equityListView;

    @FXML protected void onBuyButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/equity/buy-equity-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final BuyEquityController controller = fxmlLoader.getController();
        controller.setDialogPane(dialogPane);
        controller.setInvestor(item);
        controller.setMarkets(mainController.getItem().getMarkets());
        showDialog(dialogPane, simpleOkCallback(() -> {
            item.buyEquity(controller.create());
            refresh();
        }));
    }

    public void refresh() {
        budgetLabel.setText(String.valueOf(item.getBudget()));
        equityListView.setItems(FXCollections.observableList(item.getEquities()));
    }

}
