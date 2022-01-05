package com.put.solarsan.op.market;

import com.put.solarsan.op.asset.Company;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.market.index.Index;
import com.put.solarsan.op.settings.Settings;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StockMarket extends Market<Company> {

    private final List<Index> indexes;

    public StockMarket(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
        super(name, country, city, address, currency, margin, settings);
        this.indexes = new ArrayList<>();
    }

    public void createIndex(Index index) {
        index.start();
        indexes.add(index);
    }

    public void removeIndex(Index index) {
        index.interrupt();
        indexes.remove(index);
    }

}
