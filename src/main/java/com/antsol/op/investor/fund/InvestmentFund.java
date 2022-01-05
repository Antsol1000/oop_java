package com.antsol.op.investor.fund;

import com.antsol.op.asset.Asset;
import com.antsol.op.currency.Currency;
import com.antsol.op.investor.AbstractInvestor;
import com.antsol.op.investor.product.Buyable;
import com.antsol.op.investor.product.Sellable;
import com.antsol.op.market.Market;
import com.antsol.op.settings.Settings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;


/**
 * Real world investment fund defined by name and manager.
 * When running buys equities.
 */
@Slf4j
@Getter
public class InvestmentFund extends AbstractInvestor implements Buyable {

    private static final float FUND_BUDGET_MIN = 1000000;
    private static final float FUND_BUDGET_MAX = 10000000;
    private final String fundName;
    private final String manager;
    private final float margin;

    public InvestmentFund(float budget, Currency currency, String fundName, String manager,
                          float margin, Settings settings, List<Market<? extends Asset>> markets) {
        super(budget, currency, settings, markets);
        this.fundName = fundName;
        this.manager = manager;
        this.margin = margin;
    }

    /**
     * Factory method to create semi-random fund.
     */
    public static InvestmentFund createInvestmentFund(
            final Currency currency, final Settings settings, final List<Market<? extends Asset>> markets) {
        return new InvestmentFund(
                (float) (FUND_BUDGET_MIN + Math.random() * (FUND_BUDGET_MAX - FUND_BUDGET_MIN)),
                currency, randomDataSupplier.company().name(),
                randomDataSupplier.witcher().character(),
                0.00f, settings, markets);
    }

    /**
     * Run method for investment fund.
     * When run:
     * when bull market - 75% prob. of buy equity and 25% prob. of sell equity or participation
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
                final int i = random.nextInt(4);
                final boolean bull = settings.getChangeIndex() >= 0;
                if (i < 3 && bull || i > 2 && !bull) {
                    doRandomAssetBuy();
                } else {
                    doRandomSell();
                }
                if (random.nextInt(5) < 1 && bull) {
                    increaseBudget((float) (Math.random() * (FUND_BUDGET_MAX / 10)));
                }
                sleep(random.nextInt(settings.getTimeStep()));
            }
        } catch (InterruptedException e) {
            log.info("{} stopped.", this);
        }
    }

    /**
     * Returns total value of all possessed products.
     */
    public synchronized float getProductsTotalValue() {
        return products.stream().map(Sellable::getCurrentSellPrice).reduce(0.0f, Float::sum);
    }

    @Override
    public Sellable buy(float value, float price) {
        return new InvestmentFundParticipation(this, value, price);
    }

    @Override
    public double getAttractiveness() {
        return Math.random();
    }

    @Override
    public float getCurrentBuyPrice(float value) {
        return value * getProductsTotalValue() * (1 + margin);
    }

    @Override
    public String toString() {
        return fundName;
    }

}
