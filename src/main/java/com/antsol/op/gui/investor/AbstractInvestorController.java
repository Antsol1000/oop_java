package com.antsol.op.gui.investor;

import com.antsol.op.asset.Asset;
import com.antsol.op.gui.admin.MainController;
import com.antsol.op.gui.asset.equity.BuyEquityController;
import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.gui.investor.product.SellableController;
import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.investor.investor.PrivateInvestor;
import com.antsol.op.investor.product.BuyableWrapper;
import com.antsol.op.investor.product.Sellable;
import com.antsol.op.market.Market;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractInvestorController<I extends AbstractInvestor> extends AbstractController<I> {

    @Setter
    protected MainController mainController;
    @FXML
    private Label budgetLabel;
    @FXML
    private Label currencyLabel;
    @FXML
    private ListView<Sellable> productsListView;

    public static <I extends AbstractInvestor> EventHandler<? super MouseEvent> onInvestorMouseClick(
            final MainController mainController, final ListView<I> investors) {
        return (x) -> {
            if (!investors.getSelectionModel().isEmpty()) {
                try {
                    String resourcePath = "/%s/%s-view.fxml";
                    if (investors.getSelectionModel().getSelectedItem() instanceof InvestmentFund) {
                        resourcePath = String.format(resourcePath, "fund", "fund");
                    } else if (investors.getSelectionModel().getSelectedItem() instanceof PrivateInvestor) {
                        resourcePath = String.format(resourcePath, "investor", "investor");
                    }
                    final I investor = investors.getSelectionModel().getSelectedItem();
                    ControllerUtils.<I, AbstractInvestorController<I>>buildItemStage(
                            investor, resourcePath, y -> y.setMainController(mainController));
                } catch (Exception ignored) {
                }
            }
        };
    }

    @Override
    protected void init() {
        currencyLabel.setText(item.getCurrency().toString());
        productsListView.setOnMouseClicked(SellableController.onSellableMouseClick(this, productsListView));
        refresh();
        final Thread refresher = ControllerUtils.createAndStartRefresher(this::refresh, item.toString(),
                mainController.getItem().getSettings().getTimeStep());
        stage.setOnCloseRequest(x -> refresher.interrupt());
    }

    @FXML
    @SuppressWarnings("unchecked cast")
    private void onBuyButtonClick() throws IOException {
        ControllerUtils.<BuyableWrapper<Asset>, BuyEquityController>buildCreateDialog(
                "/equity/buy-equity-view.fxml",
                x -> {
                    x.setInvestor(item);
                    x.setMarkets(mainController.getItem().getMarkets().stream()
                            .map(m -> (Market<Asset>) m).collect(Collectors.toList()));
                },
                x -> {
                    item.buy(x);
                    refresh();
                });
    }

    public void refresh() {
        budgetLabel.setText(String.valueOf(item.getBudget()));
        productsListView.setItems(FXCollections.observableList(item.getProducts()));
    }

}
