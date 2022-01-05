package com.antsol.op.gui.admin;

import com.antsol.op.admin.Admin;
import com.antsol.op.asset.Asset;
import com.antsol.op.currency.Currency;
import com.antsol.op.gui.MainApplication;
import com.antsol.op.gui.common.AbstractController;
import com.antsol.op.gui.common.ControllerUtils;
import com.antsol.op.gui.currency.CreateCurrencyController;
import com.antsol.op.gui.currency.CurrencyController;
import com.antsol.op.gui.investor.fund.CreateFundController;
import com.antsol.op.gui.investor.fund.FundController;
import com.antsol.op.gui.investor.investor.CreateInvestorController;
import com.antsol.op.gui.investor.investor.InvestorController;
import com.antsol.op.gui.market.CreateMarketController;
import com.antsol.op.gui.market.MarketController;
import com.antsol.op.gui.settings.SettingsController;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.investor.investor.PrivateInvestor;
import com.antsol.op.market.Market;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.antsol.op.gui.common.ControllerUtils.*;

@Slf4j
public class MainController extends AbstractController<Admin> {

    @FXML
    private ListView<Currency> currencies;
    @FXML
    private ListView<Market<? extends Asset>> markets;
    @FXML
    private ListView<InvestmentFund> funds;
    @FXML
    private ListView<PrivateInvestor> investors;

    public synchronized void refresh() {
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
        final Thread refresher = ControllerUtils.createAndStartRefresher(this::refresh, item.toString(),
                item.getSettings().getTimeStep());
        stage.setOnCloseRequest(x -> {
            refresher.interrupt();
            item.interrupt();
            System.exit(0);
        });
    }

    @FXML
    private void onSettingsButtonClick() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/settings/settings-view.fxml"));
        final DialogPane dialogPane = fxmlLoader.load();
        final SettingsController controller = fxmlLoader.getController();
        controller.setItem(item.getSettings());
        showDialog(dialogPane, simpleOkCallback(controller::setSettings));
    }

    @FXML
    private void onCreateCurrencyButtonClick() throws IOException {
        ControllerUtils.<Currency, CreateCurrencyController>buildCreateDialog(
                "/currency/create-currency-view.fxml",
                getEmptyConsumer(), x -> {
                    item.createCurrency(x);
                    refresh();
                });
    }

    @FXML
    private void onCreateMarketButtonClick() throws IOException {
        ControllerUtils.<Market<?>, CreateMarketController>buildCreateDialog(
                "/market/create-market-view.fxml",
                x -> {
                    x.setSettings(item.getSettings());
                    x.setCurrencies(item.getCurrencies());
                },
                x -> {
                    item.createMarket(x);
                    refresh();
                });
    }

    @FXML
    private void onCreateFundButtonClick() throws IOException {
        ControllerUtils.<InvestmentFund, CreateFundController>buildCreateDialog(
                "/fund/create-fund-view.fxml",
                x -> {
                    x.setSettings(item.getSettings());
                    x.setCurrencies(item.getCurrencies());
                    x.setMarkets(item.getMarkets());
                },
                x -> {
                    item.createFund(x);
                    refresh();
                }
        );
    }

    @FXML
    private void onCreateInvestorButtonClick() throws IOException {
        ControllerUtils.<PrivateInvestor, CreateInvestorController>buildCreateDialog(
                "/investor/create-investor-view.fxml",
                x -> {
                    x.setCurrencies(item.getCurrencies());
                    x.setSettings(item.getSettings());
                    x.setMarkets(item.getMarkets());
                    x.setFunds(item.getFunds());
                },
                x -> {
                    item.createInvestor(x);
                    refresh();
                }
        );
    }
}