package com.antsol.op.asset;

import com.antsol.op.investor.product.Buyable;
import com.antsol.op.investor.product.Sellable;
import com.antsol.op.market.Market;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all assets.
 */
@Slf4j
@Getter
public abstract class Asset extends Thread implements Buyable {

    protected float currentPrice;
    protected final List<Record> history;
    protected final Market<? extends Asset> market;

    private final String assetName;
    private final float openingPrice;
    private float minPrice;
    private float maxPrice;
    private double attractiveness;

    private int buysFromLastCheck;
    private int sellsFromLastCheck;

    protected Asset(String assetName, float openingPrice, Market<? extends Asset> market) {
        this.assetName = assetName;
        this.openingPrice = openingPrice;
        this.currentPrice = openingPrice;
        this.minPrice = openingPrice;
        this.maxPrice = openingPrice;
        this.history = new ArrayList<>();
        this.market = market;
        this.attractiveness = 1.0;
        this.buysFromLastCheck = 0;
        this.sellsFromLastCheck = 0;
    }

    @Override
    public String toString() {
        return assetName;
    }

    /**
     * Run method for asset.
     * For every timestep it runs one assetIteration.
     * Every iteration consist of calculating new price, adding Record to history
     * and updating attractiveness.
     */
    @Override
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public void run() {
        history.add(new Record(currentPrice, new Timestamp(System.currentTimeMillis())));
        try {
            while (true) {
                assetIteration();
                sleep(market.getSettings().getTimeStep());
            }
        } catch (InterruptedException ex) {
            log.info("{} stopped.", this);
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        log.info("{} started.", this);
    }

    protected synchronized void assetIteration() {
        final float lastPrice = currentPrice;
        currentPrice = (float) (currentPrice * (1 + market.getSettings().getChangeIndex()
                - .10 + Math.random() * 2 / 10
                + (buysFromLastCheck - sellsFromLastCheck) / (buysFromLastCheck + sellsFromLastCheck + 1) / 10));
        buysFromLastCheck = 0;
        sellsFromLastCheck = 0;
        minPrice = Math.min(currentPrice, minPrice);
        maxPrice = Math.max(currentPrice, maxPrice);
        history.add(new Record(currentPrice, new Timestamp(System.currentTimeMillis())));
        updateAttractiveness(lastPrice);
    }

    private synchronized void updateAttractiveness(final float lastPrice) {
        attractiveness = (lastPrice - currentPrice) / lastPrice;
        attractiveness = Math.min(attractiveness, 0.49);
        attractiveness = Math.max(attractiveness, -0.49);
        attractiveness += 0.50;
    }

    /**
     * Enable buying this asset with given value and price.
     * Returns Equity with proper parameters.
     */
    @Override
    public Sellable buy(float value, float price) {
        buysFromLastCheck++;
        return new Equity<>(this, value, price, market.getMargin());
    }

    /**
     * Called while given Equity is sold.
     */
    public <A extends Asset> void whenSellEquity(final Equity<A> equity) {
        sellsFromLastCheck++;
    }

    @Override
    public double getAttractiveness() {
        return attractiveness;
    }

    @Override
    public float getCurrentBuyPrice(float value) {
        return value * currentPrice * (1 + market.getMargin());
    }
}
