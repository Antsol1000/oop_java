package com.put.solarsan.op.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Equity<A extends Asset<A>> {
    private final A asset;
    private float value;
    private float price;

    @Override
    public String toString() {
        return asset.toString() + String.format(" (%f)", value);
    }

}
