package com.put.solarsan.op.investor.fund;

import com.put.solarsan.op.asset.Equity;
import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.investor.AbstractInvestor;
import lombok.Getter;

@Getter
public class InvestmentFund extends AbstractInvestor {

    private final String name;
    private final String manager;

    public InvestmentFund(float budget, Currency currency, String name, String manager) {
        super(budget, currency);
        this.name = name;
        this.manager = manager;
    }

    @Override
    public void run() {

    }

    public float getEquitiesValue() {
        return equities.stream().map(Equity::getValue).reduce(0.0f, Float::sum);
    }

    @Override
    public String toString() {
        return name;
    }

}
