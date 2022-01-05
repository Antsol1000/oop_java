package com.antsol.op.investor.fund;

import com.antsol.op.investor.product.Sellable;
import lombok.Value;

/**
 * Participation in products of given investment fund.
 * Provides access to current value and original price.
 */
@Value
public class InvestmentFundParticipation implements Sellable {

    InvestmentFund investmentFund;
    float participation;
    float originalPrice;

    @Override
    public String toString() {
        return investmentFund.toString() + String.format(" (%f)", participation);
    }

    @Override
    public float getCurrentSellPrice() {
        return participation * investmentFund.getProductsTotalValue() * (1 - investmentFund.getMargin());
    }

    @Override
    public double getAttractiveness() {
        double attractiveness = (getCurrentSellPrice() - originalPrice) / originalPrice;
        attractiveness = Math.min(attractiveness, 0.49);
        attractiveness = Math.max(attractiveness, -0.49);
        attractiveness += 0.5;
        return attractiveness;
    }

    @Override
    public void whenSold() {

    }
}
