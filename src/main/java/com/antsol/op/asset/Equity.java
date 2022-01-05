package com.antsol.op.asset;

import com.antsol.op.investor.product.Sellable;
import lombok.Value;

/**
 * Decorator for Asset.
 * Provides buy and sell mechanism.
 */
@Value
public class Equity<A extends Asset> implements Sellable {

    A asset;
    float share;
    float originalPrice;
    float margin;

    @Override
    public String toString() {
        return asset.toString() + String.format(" (%f)", share);
    }

    @Override
    public float getCurrentSellPrice() {
        return share * asset.getCurrentPrice() * (1 - margin);
    }

    @Override
    public double getAttractiveness() {
        double attractiveness = (getCurrentSellPrice() - originalPrice) / originalPrice;
        attractiveness = Math.min(attractiveness, 0.49);
        attractiveness = Math.max(attractiveness, -0.49);
        attractiveness += 0.50;
        return attractiveness;
    }

    @Override
    public void whenSold() {
        asset.whenSellEquity(this);
    }

}
