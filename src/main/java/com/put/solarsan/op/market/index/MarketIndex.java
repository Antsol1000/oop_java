package com.put.solarsan.op.market.index;

import com.put.solarsan.op.asset.Company;
import com.put.solarsan.op.asset.Record;
import com.put.solarsan.op.market.StockMarket;
import com.put.solarsan.op.settings.Settings;

import java.sql.Timestamp;
import java.util.stream.Collectors;

public class MarketIndex extends Index {

    private final int n;

    public MarketIndex(String name, StockMarket market, Settings settings, int n) {
        super(name, market, settings);
        this.n = n;
        updateIndex();
    }

    public void updateIndex() {
        this.companies = market.getAssetsList()
                .stream()
                .sorted((Company x, Company y) -> Float.compare(x.getCapital(), y.getCapital()))
                .skip(market.getAssetsList().size() - n)
                .collect(Collectors.toList());
    }

    @Override
    public void run() {
        try {
            while (true) {
                updateIndex();
                history.add(new Record(getValue(), new Timestamp(System.currentTimeMillis())));
                sleep(settings.getTimeStep());
            }
        } catch (InterruptedException ex) {
            System.out.println(this + " stopped.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + this.n;
    }
}
