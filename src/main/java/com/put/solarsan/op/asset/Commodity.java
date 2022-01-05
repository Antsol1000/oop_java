package com.put.solarsan.op.asset;

import com.put.solarsan.op.settings.Settings;
import lombok.Getter;

@Getter
public class Commodity extends Asset<Commodity> {

    private final String unit;

    public Commodity(String name, float openingPrice, Settings settings, String unit) {
        super(name, openingPrice, settings);
        this.unit = unit;
    }

}
