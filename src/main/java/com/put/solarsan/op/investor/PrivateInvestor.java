package com.put.solarsan.op.investor;

import com.put.solarsan.op.currency.Currency;
import com.put.solarsan.op.investor.fund.InvestmentFundParticipation;
import lombok.Getter;

import java.util.List;

@Getter
public class PrivateInvestor extends AbstractInvestor {

    private final String firstName;
    private final String lastName;

    private List<InvestmentFundParticipation> participations;

    public PrivateInvestor(float budget, Currency currency, String firstName, String lastName) {
        super(budget, currency);
        this.firstName = firstName;
        this.lastName = lastName;
        this.participations = List.of();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public void run() {

    }

    public void buyParticipation(final InvestmentFundParticipation participation) {
        participations.add(participation);
        budget -= participation.getPrice();
    }

    public void sellParticipation(final InvestmentFundParticipation participation, float price) {
        participations.remove(participation);
        budget += price;
    }

}
