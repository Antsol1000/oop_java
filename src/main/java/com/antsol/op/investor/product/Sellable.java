package com.antsol.op.investor.product;

/**
 * Basic interface for all product which can be sold.
 * (i.e. equity, participation)
 */
public interface Sellable {

    /**
     * Value from (0, 1) range.
     * If greater, the product is more worth-selling for investors.
     */
    double getAttractiveness();

    /**
     * Returns current sell price (with margin included).
     */
    float getCurrentSellPrice();

    /**
     * Returns price for which the product was bought.
     */
    float getOriginalPrice();

    /**
     * Called when product is sold.
     */
    void whenSold();

}
