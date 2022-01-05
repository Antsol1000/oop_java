package com.put.solarsan.op.asset;

import com.put.solarsan.op.settings.Settings;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Asset<A extends Asset<A>> extends Thread {

    private final String assetName;
    private final float openingPrice;
    private float minPrice;
    private float maxPrice;
    private float currentPrice;

    private final List<Record> history;

    private final Settings settings;

    public Asset(String name, float openingPrice, Settings settings) {
        this.assetName = name;
        this.openingPrice = openingPrice;
        this.settings = settings;
        this.currentPrice = openingPrice;
        this.minPrice = openingPrice;
        this.maxPrice = openingPrice;
        this.history = new ArrayList<>();
    }

    @Override
    public String toString() {
        return assetName;
    }

    @Override
    public void run() {
        history.add(new Record(currentPrice, new Timestamp(System.currentTimeMillis())));
        try {
            while (true) {
                currentPrice = (float) ((1 + settings.getChangeIndex() - .10 + Math.random() * 2 / 10) * currentPrice);
                minPrice = Math.min(currentPrice, minPrice);
                maxPrice = Math.max(currentPrice, maxPrice);
                history.add(new Record(currentPrice, new Timestamp(System.currentTimeMillis())));
                sleep(settings.getTimeStep());
            }
        } catch (InterruptedException ex) {
            System.out.println(this + " stopped.");
        }
    }

}
