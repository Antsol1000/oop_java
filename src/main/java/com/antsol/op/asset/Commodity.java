package com.antsol.op.asset;

import com.antsol.op.market.CommodityMarket;
import lombok.Getter;

/**
 * Class represent commodity like oil or gold.
 */
@Getter
public class Commodity extends Asset {

    private final String unit;

    public Commodity(String name, float openingPrice, String unit, CommodityMarket market) {
        super(name, openingPrice, market);
        this.unit = unit;
    }

}
