package com.antsol.op.gui.investor.product;

import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.gui.investor.AbstractInvestorController;
import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.investor.product.Sellable;
import com.antsol.op.investor.product.SellableWrapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

public class SellableController extends AbstractController<Sellable> {

    @FXML
    private Label nameLabel;
    @FXML
    private Label originalPriceLabel;
    @FXML
    private Label currentPriceLabel;
    @Setter
    private AbstractInvestorController<?> investorController;

    public static EventHandler<? super MouseEvent> onSellableMouseClick(
            final AbstractInvestorController<? extends AbstractInvestor> investorController,
            final ListView<Sellable> products) {
        return (x) -> {
            if (!products.getSelectionModel().isEmpty()) {
                try {
                    final Sellable product = products.getSelectionModel().getSelectedItem();
                    ControllerUtils.<Sellable, SellableController>buildItemStage(
                            product, "/product/sellable-view.fxml", y -> y.setInvestorController(investorController));
                } catch (Exception ignored) {
                }
            }
        };
    }

    @Override
    protected void init() {
        nameLabel.setText(item.toString());
        originalPriceLabel.setText(String.valueOf(item.getOriginalPrice()));
        currentPriceLabel.setText(String.valueOf(item.getCurrentSellPrice()));
    }

    @FXML
    private void onRefreshButtonClick() {
        currentPriceLabel.setText(String.valueOf(item.getCurrentSellPrice()));
    }

    @FXML
    private void onSellButtonClick() {
        final SellableWrapper<?> product
                = new SellableWrapper<>(item, Float.parseFloat(currentPriceLabel.getText()));
        investorController.getItem().sell(product);
        investorController.refresh();
        stage.close();
    }

}
