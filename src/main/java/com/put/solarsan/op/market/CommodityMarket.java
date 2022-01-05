package com.put.solarsan.op.market;

import com.put.solarsan.op.asset.Commodity;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.settings.Settings;

public class CommodityMarket extends Market<Commodity> {

    public CommodityMarket(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
        super(name, country, city, address, currency, margin, settings);
    }
}
