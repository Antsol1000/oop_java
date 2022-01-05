package com.put.solarsan.op.asset;

import com.put.solarsan.op.settings.Settings;
import lombok.Getter;

@Getter
public class Company extends Asset<Company> {

    private final String ipoDate;
    private final float ipoShareValue;
    private final float capital;
    private final float profit;
    private final float revenue;
    private int tradingVolume;
    private float totalSales;

    public Company(String name, float openingPrice, Settings settings, String ipoDate,
                   float ipoShareValue, float capital, float profit, float revenue) {
        super(name, openingPrice, settings);
        this.ipoDate = ipoDate;
        this.ipoShareValue = ipoShareValue;
        this.capital = capital;
        this.profit = profit;
        this.revenue = revenue;
        this.tradingVolume = 0;
        this.totalSales = 0;
    }

}
