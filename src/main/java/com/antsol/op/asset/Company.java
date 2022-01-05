package com.antsol.op.asset;

import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.investor.product.Sellable;
import com.antsol.op.investor.product.SellableWrapper;
import com.antsol.op.market.StockMarket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class represents real world company.
 * With name, fund date, ipo share value, capital.
 * It monitors company's profit and revenue.
 * It controls number of shares on market.
 */
@Slf4j
@Getter
public class Company extends Asset {

    private final String ipoDate;
    private final float ipoShareValue;
    private final float capital;
    private float profit;
    private float revenue;
    private long shareLimit;
    private long tradingVolume;
    private boolean sellingStocks;
    private boolean hardStopSellingStocks;

    public Company(String name, float openingPrice, String ipoDate, float ipoShareValue,
                   long shareLimit, float capital, float profit, float revenue, StockMarket market) {
        super(name, openingPrice, market);
        this.ipoDate = ipoDate;
        this.ipoShareValue = ipoShareValue;
        this.shareLimit = shareLimit;
        this.capital = capital;
        this.profit = profit;
        this.revenue = revenue;
        this.tradingVolume = 0;
        this.sellingStocks = true;
        this.hardStopSellingStocks = false;
    }

    /**
     * Run method for Company.
     * In every iteration it does assetIteration but also generate profit.
     * It can with 10% probability increase total number of shares available on market.
     */
    @Override
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public void run() {
        history.add(new Record(currentPrice, new Timestamp(System.currentTimeMillis())));
        try {
            while (true) {
                assetIteration();
                generateProfit();
                if (Math.random() < 0.1) {
                    increaseShareLimit();
                }
                sleep(market.getSettings().getTimeStep());
            }
        } catch (InterruptedException ex) {
            log.info("{} stopped.", this);
        }
    }

    /**
     * Buys back all equities associated with this Company from given investors.
     */
    public synchronized void buyBackAllEquities(final List<? extends AbstractInvestor> investors) {
        for (AbstractInvestor investor : investors) {
            investor.getProducts().stream()
                    .filter(x -> x instanceof Equity<?>)
                    .filter(x -> ((Equity<?>) x).getAsset().equals(this))
                    .map(x -> new SellableWrapper<>(x, x.getCurrentSellPrice()))
                    .collect(Collectors.toList())
                    .forEach(investor::sell);
        }
    }

    /**
     * Increases share limit by 50%.
     */
    public synchronized void increaseShareLimit() {
        shareLimit += shareLimit / 2;
    }

    private void generateProfit() {
        revenue += Math.random() * (50000);
        profit += -1000 + Math.random() * (5000);
    }

    /**
     * Returns true if company can start selling stocks.
     */
    public boolean canStartSelling() {
        return !sellingStocks && tradingVolume < shareLimit;
    }

    /**
     * Returns true if company can stop selling stocks.
     */
    public boolean canStopSelling() {
        return sellingStocks;
    }

    /**
     * Starts selling stock.
     */
    public synchronized void hardStartSelling() {
        sellingStocks = true;
        hardStopSellingStocks = false;
    }

    /**
     * Stops selling stock.
     * Stocks can be sold only after call to hardStartSelling.
     */
    public synchronized void hardStopSelling() {
        sellingStocks = false;
        hardStopSellingStocks = true;
    }

    /**
     * Buy method, which ensures that stock can be bought.
     * It also ensures that only whole number of shares can be bought.
     */
    @Override
    public Sellable buy(float value, float price) {
        if (!sellingStocks) {
            throw new IllegalStateException(this + " company does not sell shares currently");
        }
        final int correct_value = (int) value;
        tradingVolume += correct_value;
        if (tradingVolume >= shareLimit) {
            sellingStocks = false;
        }
        return super.buy(correct_value, price);
    }

    /**
     * Called when stock is sold, controls total number of shares.
     */
    @Override
    public <A extends Asset> void whenSellEquity(Equity<A> equity) {
        tradingVolume -= equity.getShare();
        if (!hardStopSellingStocks && canStartSelling()) {
            sellingStocks = true;
        }
        super.whenSellEquity(equity);
    }

    @Override
    public float getMaxValueForPrice(float budget, float price) {
        return Math.min(super.getMaxValueForPrice(budget, price), shareLimit - tradingVolume);
    }

    /**
     * Returns total value of all shares available on market.
     */
    public float getTotalSales() {
        return getCurrentPrice() * tradingVolume;
    }
}
