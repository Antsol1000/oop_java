package com.put.solarsan.op.market.index;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.asset.Company;
import com.put.solarsan.op.asset.Record;
import com.put.solarsan.op.market.StockMarket;
import com.put.solarsan.op.settings.Settings;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Index extends Thread {

    protected final String name;
    protected final StockMarket market;
    protected final Settings settings;

    protected final List<Record> history;

    protected List<Company> companies;

    public Index(String name, StockMarket market, Settings settings) {
        this.name = name;
        this.market = market;
        this.settings = settings;
        this.companies = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                history.add(new Record(getValue(), new Timestamp(System.currentTimeMillis())));
                sleep(settings.getTimeStep());
            }
        } catch (InterruptedException ex) {
            System.out.println(this + " stopped.");
        }
    }

    public List<Record> getHistory() {
        return history;
    }

    public void add(Company stock) {
        this.companies.add(stock);
    }

    public float getValue() {
        return companies.stream().map(Asset::getCurrentPrice).reduce(Float::sum).get();
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Company> getCompanies() {
        return companies;
    }
}
