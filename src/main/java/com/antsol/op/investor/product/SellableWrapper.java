package com.antsol.op.investor.product;

import lombok.Value;

/**
 * Decorator for sellable product.
 * Used when price for sell is decided.
 */
@Value
public class SellableWrapper<S extends Sellable> {

    S product;
    float price;

}
