package com.antsol.op.asset;

import com.antsol.op.currency.Currency;
import com.antsol.op.market.CurrencyMarket;
import lombok.Getter;

/**
 * CurrencyAsset class represents Currency
 * that is present on some market
 * and can be bought or sold.
 */
@Getter
public class CurrencyAsset extends Asset {

    private final Currency currency;

    public CurrencyAsset(float openingPrice, Currency currency, CurrencyMarket market) {
        super(currency.getName(), openingPrice, market);
        this.currency = currency;
    }

}
