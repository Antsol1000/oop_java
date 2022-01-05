package com.antsol.op.market;

import com.antsol.op.asset.Asset;
import com.antsol.op.currency.Currency;
import com.antsol.op.settings.Settings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all markets.
 * Provides access to market basic information.
 * @param <A> asset type which is present on market
 */
@Slf4j
@Getter
public abstract class Market<A extends Asset> {

    private final String name;
    private final String country;
    private final String city;
    private final String address;
    private final Currency currency;
    private final float margin;
    private final Settings settings;
    protected final List<A> assetsList;

    protected Market(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
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

    /**
     * Adds and starts new asset.
     */
    public void createAsset(A asset) {
        assetsList.add(asset);
        log.info("Asset {} created.", asset);
        asset.start();
    }

    /**
     * Stops and removes asset.
     */
    public void removeAsset(A asset) {
        asset.interrupt();
        assetsList.remove(asset);
        log.info("Asset {} removed.", asset);
    }

    /**
     * @return type of this market.
     */
    public MarketType getType() {
        if (this instanceof StockMarket) {
            return MarketType.STOCK;
        } else if (this instanceof CurrencyMarket) {
            return MarketType.CURRENCY;
        } else if (this instanceof CommodityMarket) {
            return MarketType.COMMODITY;
        } else {
            throw new RuntimeException("unknown market type");
        }
    }
}
