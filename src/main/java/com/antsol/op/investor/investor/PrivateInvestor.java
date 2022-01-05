package com.antsol.op.investor.investor;

import com.antsol.op.asset.Asset;
import com.antsol.op.currency.Currency;
import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.investor.fund.InvestmentFund;
import com.antsol.op.investor.product.Buyable;
import com.antsol.op.market.Market;
import com.antsol.op.settings.Settings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Real world private investor defined by first and last name.
 * When running buys equities and funds participations.
 */
@Slf4j
@Getter
public class PrivateInvestor extends AbstractInvestor {

    private static final float PRIVATE_BUDGET_MIN = 1000;
    private static final float PRIVATE_BUDGET_MAX = 100000;
    private final String firstName;
    private final String lastName;
    private final List<InvestmentFund> funds;

    public PrivateInvestor(float budget, Currency currency, String firstName, String lastName,
                           Settings settings, List<Market<?>> markets, List<InvestmentFund> funds) {
        super(budget, currency, settings, markets);
        this.firstName = firstName;
        this.lastName = lastName;
        this.funds = funds;
    }

    /**
     * Factory method to create semi-random investor.
     */
    public static PrivateInvestor createPrivateInvestor(
            final Currency currency, final Settings settings,
            final List<Market<? extends Asset>> markets, final List<InvestmentFund> funds) {
        return new PrivateInvestor(
                (float) (PRIVATE_BUDGET_MIN + Math.random() * (PRIVATE_BUDGET_MAX - PRIVATE_BUDGET_MIN)),
                currency, randomDataSupplier.name().firstName(),
                randomDataSupplier.name().lastName(), settings, markets, funds);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    /**
     * Run method for private investor.
     * When run:
     * with probability 12,5% buy fund participation,
     * when bull market - 62,5% prob. of buy equity and 25% prob. of sell equity or participation
     * when bear market - in reverse
     * And can increase budget with 20% prob. but only when bull market.
     * Buy and sell of products are based on their attractiveness.
     */
    @Override
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    public void run() {
        final Random random = new Random();
        try {
            while (true) {
                final int i = random.nextInt(8);
                final boolean bull = settings.getChangeIndex() >= 0;
                if (i > 6) {
                    doRandomParticipationBuy();
                } else if (i < 5 && bull || i > 4 && !bull) {
                    doRandomAssetBuy();
                } else {
                    doRandomSell();
                }
                if (random.nextInt(5) < 1 && bull) {
                    increaseBudget((float) (Math.random() * (PRIVATE_BUDGET_MAX / 10)));
                }
                sleep(random.nextInt(2 * settings.getTimeStep()));
            }
        } catch (InterruptedException e) {
            log.info("{} stopped.", this);
        }
    }

    private void doRandomParticipationBuy() {
        List<Buyable> participations;
        synchronized (new Object()) {
            participations = funds.stream()
                    .filter(x -> x.getCurrency().equals(currency))
                    .collect(Collectors.toList());
        }
        generateBuy(participations).ifPresent(this::buy);
    }

}
