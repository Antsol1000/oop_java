package com.put.solarsan.op.gui;

import com.put.solarsan.op.admin.Admin;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.gui.common.AbstractController;
import com.put.solarsan.op.gui.currency.CreateCurrencyController;
import com.put.solarsan.op.gui.currency.CurrencyController;
import com.put.solarsan.op.gui.investor.fund.CreateFundController;
import com.put.solarsan.op.gui.investor.CreateInvestorController;
import com.put.solarsan.op.gui.investor.fund.FundController;
import com.put.solarsan.op.gui.investor.InvestorController;
import com.put.solarsan.op.gui.market.CreateMarketController;
import com.put.solarsan.op.gui.market.MarketController;
import com.put.solarsan.op.gui.settings.SettingsController;
import com.put.solarsan.op.investor.PrivateInvestor;
import com.put.solarsan.op.investor.fund.InvestmentFund;
import com.put.solarsan.op.market.Market;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;

import java.io.IOException;

import static com.put.solarsan.op.gui.common.ControllerUtils.showDialog;
import static com.put.solarsan.op.gui.common.ControllerUtils.simpleOkCallback;

public class MainController extends AbstractController<Admin> {

    @FXML protected ListView<Currency> currencies;
    @FXML protected ListView<Market<?>> markets;
    @FXML protected ListView<InvestmentFund> funds;
    @FXML protected ListView<PrivateInvestor> investors;

    public void refresh() {
        currencies.setItems(FXCollections.observableList(item.getCurrencies()));
        markets.setItems(FXCollections.observableList(item.getMarkets()));
        funds.setItems(FXCollections.observableList(item.getFunds()));
        investors.setItems(FXCollections.observableList(item.getInvestors()));
    }

    @Override
    protected void init() {
        refresh();
        currencies.setOnMouseClicked(CurrencyController.onCurrencyMouseClick(this, currencies));
        markets.setOnMouseClicked(MarketController.onMarketMouseClick(this, markets));
        funds.setOnMouseClicked(FundController.onInvestorMouseClick(this, funds));
        investors.setOnMouseClicked(InvestorController.onInvestorMouseClick(this, investors));
    }

    @FXML protected void onSettingsButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/settings/settings-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final SettingsController controller = fxmlLoader.getController();
        controller.setItem(item.getSettings());
        showDialog(dialogPane, simpleOkCallback(controller::setSettings));
    }

    @FXML protected void onCreateCurrencyButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/currency/create-currency-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final CreateCurrencyController controller = fxmlLoader.getController();
        showDialog(dialogPane, simpleOkCallback(() -> {
            item.createCurrency(controller.create());
            refresh();
        }));
    }

    @FXML protected void onCreateMarketButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/market/create-market-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final CreateMarketController controller = fxmlLoader.getController();
        controller.setSettings(item.getSettings());
        controller.setCurrencies(item.getCurrencies());
        showDialog(dialogPane, simpleOkCallback(() -> {
            item.createMarket(controller.create());
            refresh();
        }));
    }

    @FXML protected void onCreateFundButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fund/create-fund-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final CreateFundController controller = fxmlLoader.getController();
        controller.setCurrencies(item.getCurrencies());
        showDialog(dialogPane, simpleOkCallback(() -> {
            item.createFund(controller.create());
            refresh();
        }));
    }

    @FXML protected void onCreateInvestorButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/investor/create-investor-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final CreateInvestorController controller = fxmlLoader.getController();
        controller.setCurrencies(item.getCurrencies());
        showDialog(dialogPane, simpleOkCallback(() -> {
            item.createInvestor(controller.create());
            refresh();
        }));
    }
}