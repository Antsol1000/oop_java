package com.put.solarsan.op.investor.fund;

import lombok.Value;

@Value
public class InvestmentFundParticipation {

    InvestmentFund investmentFund;
    float participation;
    float price;

    public float getValue() {
        return participation * investmentFund.getEquitiesValue();
    }
}
