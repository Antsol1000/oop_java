package com.put.solarsan.op.market;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.settings.Settings;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.put.solarsan.op.market.MarketType.*;

@Getter
public abstract class Market<A extends Asset<A>> implements Runnable {

    protected List<A> assetsList;

    private final String name;
    private final String country;
    private final String city;
    private final String address;
    private final Currency currency;
    private final float margin;

    private final Settings settings;

    public Market(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.currency = currency;
        this.margin = margin;
        this.settings = settings;
        this.assetsList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void run() {
    }

    public void createAsset(A asset) {
        assetsList.add(asset);
        asset.start();
    }

    public void removeAsset(A asset) {
        asset.interrupt();
        assetsList.remove(asset);
    }

    public MarketType getType() {
        if (this instanceof StockMarket) {
            return STOCK;
        } else if (this instanceof CurrencyMarket) {
            return CURRENCY;
        } else if (this instanceof CommodityMarket) {
            return COMMODITY;
        } else {
            throw new RuntimeException("unknown market type");
        }
    }
}
