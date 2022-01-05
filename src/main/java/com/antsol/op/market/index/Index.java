package com.antsol.op.market.index;

import com.antsol.op.asset.Asset;
import com.antsol.op.asset.Company;
import com.antsol.op.asset.Record;
import com.antsol.op.market.StockMarket;
import com.antsol.op.settings.Settings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Static index - companies are added to this index on command.
 * Provides history of index values.
 */
@Slf4j
public class Index extends Thread {

    protected final String name;
    protected final StockMarket market;
    protected final Settings settings;

    @Getter
    protected final List<Record> history;
    @Getter
    protected List<Company> companies;

    public Index(String name, StockMarket market, Settings settings) {
        this.name = name;
        this.market = market;
        this.settings = settings;
        this.companies = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    /**
     * When run record value of index.
     */
    @Override
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public void run() {
        try {
            while (true) {
                history.add(new Record(getValue(), new Timestamp(System.currentTimeMillis())));
                sleep(settings.getTimeStep());
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

    /**
     * Add company to index.
     */
    public void add(final Company company) {
        this.companies.add(company);
    }

    /**
     * Returns current value of index.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public synchronized float getValue() {
        return companies.stream().map(Asset::getCurrentPrice).reduce(Float::sum).get();
    }

    @Override
    public String toString() {
        return name;
    }

}
