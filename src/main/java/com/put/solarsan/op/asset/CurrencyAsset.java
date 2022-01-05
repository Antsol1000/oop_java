package com.put.solarsan.op.asset;

import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.settings.Settings;
import lombok.Getter;

@Getter
public class CurrencyAsset extends Asset<CurrencyAsset> {

    private final Currency currency;

    public CurrencyAsset(float openingPrice, Currency currency, Settings settings) {
        super(currency.getName(), openingPrice, settings);
        this.currency = currency;
    }

}
