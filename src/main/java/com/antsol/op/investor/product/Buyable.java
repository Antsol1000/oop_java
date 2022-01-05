package com.antsol.op.investor.product;

/**
 * Basic interface for all things which products can be bought by investors.
 * (i.e. Assets (investors can buy equity), InvestmentFund (investors can buy participation)).
 */
public interface Buyable {

    /**
     * Value from (0, 1) range.
     * If greater, the product is more worth-buying for investors.
     */
    double getAttractiveness();

    /**
     * Returns current buy price (with margin included).
     */
    float getCurrentBuyPrice(final float value);

    /**
     * Returns maximum value of product which can be bought with given budget.
     */
    default float getMaxValueForPrice(final float budget, final float price) {
        return price == 0 ? 0 : budget / price;
    }

    /**
     * Wrap product with given value and price and creates Sellable product.
     * (i.e. creates Equity when some Asset is bought)
     */
    Sellable buy(final float value, final float price);
}
