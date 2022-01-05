package com.antsol.op.admin;

import com.antsol.op.asset.Asset;
import com.antsol.op.asset.Equity;
import com.antsol.op.currency.Currency;
import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.investor.fund.InvestmentFundParticipation;
import com.antsol.op.investor.investor.PrivateInvestor;
import com.antsol.op.investor.product.SellableWrapper;
import com.antsol.op.market.Market;
import com.antsol.op.settings.Settings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin class provides facade to use create or remove currencies,
 * markets, investors or funds.
 * Also, it's used to manage world parameters.
 * While running, it dynamically creates investors and funds.
 */
@Slf4j
@Getter
public class Admin extends Thread {

    private final List<Currency> currencies;
    private final List<Market<? extends Asset>> markets;
    private final List<PrivateInvestor> investors;
    private final List<InvestmentFund> funds;

    private final Settings settings;

    public Admin() {
        this.currencies = new ArrayList<>();
        this.markets = new ArrayList<>();
        this.funds = new ArrayList<>();
        this.investors = new ArrayList<>();
        this.settings = new Settings();
        settings.setChangeIndex(0.01f);
        settings.setTimeStep(1000);
    }

    /**
     * Run method for Admin.
     * Ensures that number of investors is not less than 3 * number of assets
     * and number of funds is not less than number of assets.
     */
    @Override
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public void run() {
        try {
            while (true) {
                sleep(settings.getTimeStep() * 3L);
                for (Currency c : currencies) {
                    final long assetCount = markets.stream()
                            .filter(x -> x.getCurrency().equals(c))
                            .mapToLong(x -> x.getAssetsList().size())
                            .sum();
                    while (investors.size() < assetCount * 3) {
                        createInvestor(PrivateInvestor.createPrivateInvestor(c, settings, markets, funds));
                    }
                    while (funds.size() < assetCount) {
                        createFund(InvestmentFund.createInvestmentFund(c, settings, markets));
                    }
                }
            }
        } catch (InterruptedException e) {
            log.info("Admin stopped");
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        log.info("Admin started");
    }

    /**
     * Adds new Currency to currency list.
     */
    public void createCurrency(Currency currency) {
        currencies.add(currency);
        log.info("Currency {} created.", currency);
    }

    /**
     * Adds new Market to market list.
     */
    public <A extends Asset> void createMarket(Market<A> market) {
        markets.add(market);
        log.info("Market {} created.", market);
    }

    /**
     * Adds new Fund to fund list.
     * Runs created Fund.
     */
    public void createFund(InvestmentFund fund) {
        funds.add(fund);
        log.info("Fund {} created.", fund);
        fund.start();
    }

    /**
     * Adds new Investor to investor list.
     * Runs created Investor.
     */
    public void createInvestor(PrivateInvestor investor) {
        investors.add(investor);
        log.info("Investor {} created", investor);
        investor.start();
    }

    /**
     * Removes Market.
     * Buy back all products associated with assets from removed Market.
     */
    public <A extends Asset> void removeMarket(Market<A> market) {
        markets.remove(market);
        log.info("Market {} removed.", market);
        buyBackAssetsFromMarket(market, funds);
        buyBackAssetsFromMarket(market, investors);
    }

    @SuppressWarnings("unchecked")
    private <A extends Asset> void buyBackAssetsFromMarket(
            final Market<A> market, final List<? extends AbstractInvestor> investors) {
        investors.forEach(x -> x.getProducts().stream()
                .filter(y -> y instanceof Equity<?>)
                .filter(y -> market.getAssetsList().contains(((Equity<A>) y).getAsset()))
                .forEach(y -> x.sell(new SellableWrapper<>(y, y.getCurrentSellPrice()))));
    }

    /**
     * Stops and removes Fund.
     * Buy back all participations which were generated by Fund.
     */
    public void removeFund(InvestmentFund fund) {
        fund.interrupt();
        funds.remove(fund);
        log.info("Fund {} removed", fund);
        investors.forEach(x -> x.getProducts().stream()
                .filter(p -> p instanceof InvestmentFundParticipation)
                .filter(p -> ((InvestmentFundParticipation) p).getInvestmentFund().equals(fund))
                .forEach(p -> fund.sell(new SellableWrapper<>(p, p.getCurrentSellPrice()))));
    }

    /**
     * Stops and removes Investor.
     */
    public void removeInvestor(PrivateInvestor investor) {
        investor.interrupt();
        investors.remove(investor);
        log.info("Investor {} removed", investor);
    }

    @Override
    public String toString() {
        return "Admin";
    }
}
