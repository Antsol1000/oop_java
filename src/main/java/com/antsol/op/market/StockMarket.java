package com.antsol.op.market;

import com.antsol.op.asset.Company;
import com.antsol.op.currency.Currency;
import com.antsol.op.market.index.Index;
import com.antsol.op.settings.Settings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Market with stocks.
 * On this market there are also indexes.
 */
@Slf4j
@Getter
public class StockMarket extends Market<Company> {

    private final List<Index> indexes;

    public StockMarket(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
        super(name, country, city, address, currency, margin, settings);
        this.indexes = new ArrayList<>();
    }

    /**
     * Adds and starts new index.
     */
    public void createIndex(Index index) {
        indexes.add(index);
        log.info("Index {} created.", index);
        index.start();
    }

    /**
     * Stops and removes index.
     */
    public void removeIndex(Index index) {
        index.interrupt();
        indexes.remove(index);
        log.info("Index {} removed.", index);
    }

}
