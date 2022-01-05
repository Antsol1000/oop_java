package com.put.solarsan.op.market;

import com.put.solarsan.op.asset.CurrencyAsset;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.settings.Settings;

public class CurrencyMarket extends Market<CurrencyAsset> {

    public CurrencyMarket(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
        super(name, country, city, address, currency, margin, settings);
    }
}
