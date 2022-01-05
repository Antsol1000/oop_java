package com.antsol.op.market;

import com.antsol.op.asset.CurrencyAsset;
import com.antsol.op.currency.Currency;
import com.antsol.op.settings.Settings;

/**
 * Market with currencies other than the one which is used to buy or sell assets.
 */
public class CurrencyMarket extends Market<CurrencyAsset> {

    public CurrencyMarket(String name, String country, String city, String address, Currency currency, float margin, Settings settings) {
        super(name, country, city, address, currency, margin, settings);
    }
}
