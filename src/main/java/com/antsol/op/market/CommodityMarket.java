package com.antsol.op.market;

import com.antsol.op.asset.Commodity;
import com.antsol.op.currency.Currency;
import com.antsol.op.settings.Settings;

/**
 * Market with commodities like oil or gold.
 */
public class CommodityMarket extends Market<Commodity> {

    public CommodityMarket(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
        super(name, country, city, address, currency, margin, settings);
    }
}
