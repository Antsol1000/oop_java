package com.antsol.op.investor.product;

import lombok.Value;

/**
 * Decorator for buyable product.
 * Used when value and price for buy is decided.
 */
@Value
public class BuyableWrapper<B extends Buyable> {

    B product;
    float value;
    float price;

}
