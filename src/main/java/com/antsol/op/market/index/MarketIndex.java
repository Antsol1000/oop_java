package com.antsol.op.market.index;

import com.antsol.op.asset.Company;
import com.antsol.op.asset.Record;
import com.antsol.op.market.StockMarket;
import com.antsol.op.settings.Settings;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.stream.Collectors;

/**
 * Dynamic index - n biggest companies from market.
 * Provides history of index prices.
 */
@Slf4j
public class MarketIndex extends Index {

    private final int n;

    public MarketIndex(String name, StockMarket market, Settings settings, int n) {
        super(name, market, settings);
        this.n = n;
        updateIndex();
    }

    private synchronized void updateIndex() {
        this.companies = market.getAssetsList()
                .stream()
                .sorted((Company x, Company y) -> Float.compare(x.getCapital(), y.getCapital()))
                .skip(market.getAssetsList().size() - n)
                .collect(Collectors.toList());
    }

    /**
     * When run, records index prices over time and update index.
     */
    @Override
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public void run() {
        try {
            while (true) {
                updateIndex();
                history.add(new Record(getValue(), new Timestamp(System.currentTimeMillis())));
                sleep(settings.getTimeStep());
            }
        } catch (InterruptedException ex) {
            log.info("{} stopped.", this);
        }
    }

    @Override
    public String toString() {
        return super.toString() + this.n;
    }
}
