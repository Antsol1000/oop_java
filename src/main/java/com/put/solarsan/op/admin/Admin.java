package com.put.solarsan.op.admin;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.investor.PrivateInvestor;
import com.put.solarsan.op.investor.fund.InvestmentFund;
import com.put.solarsan.op.market.Market;
import com.put.solarsan.op.settings.Settings;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Admin {
    private List<Market<?>> markets;
    private List<Currency> currencies;
    private List<PrivateInvestor> investors;
    private List<InvestmentFund> funds;
    private Settings settings;

    public Admin() {
        this.currencies = new ArrayList<>();
        this.markets = new ArrayList<>();
        this.funds = new ArrayList<>();
        this.investors = new ArrayList<>();
        this.settings = new Settings();
        settings.setChangeIndex(0.01f);
        settings.setTimeStep(1000);
    }

    public void createCurrency(Currency currency) {
        currencies.add(currency);
    }

    public <A extends Asset<A>> void createMarket(Market<A> market) {
        markets.add(market);
    }

    public void createInvestor(PrivateInvestor investor) {
        investors.add(investor);
    }

    public void createFund(InvestmentFund fund) {
        funds.add(fund);
    }

    public void removeCurrency(Currency currency) {
        currencies.remove(currency);
    }

    public <A extends Asset<A>> void removeMarket(Market<A> market) {
        markets.remove(market);
    }

    public void removeInvestor(PrivateInvestor investor) {
        investors.remove(investor);
    }

    public void removeFund(InvestmentFund fund) {
        funds.remove(fund);
    }
}
