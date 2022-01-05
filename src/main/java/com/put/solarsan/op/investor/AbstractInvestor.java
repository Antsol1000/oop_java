package com.put.solarsan.op.investor;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.asset.Equity;
import com.put.solarsan.op.currency.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class AbstractInvestor implements Runnable {

    protected float budget;
    protected final Currency currency;

    protected List<Equity<? extends Asset<?>>> equities;

    protected AbstractInvestor(float budget, Currency currency) {
        this.budget = budget;
        this.currency = currency;
        this.equities = new ArrayList<>();
    }

    public <A extends Asset<A>> void buyEquity(final Equity<A> equity) {
        equities.add(equity);
        budget -= equity.getPrice();
    }

    public <A extends Asset<A>> void sellEquity(final Equity<A> equity, float price) {
        equities.remove(equity);
        budget += price;
    }

}
