package com.antsol.op.investor;

import com.antsol.op.asset.Asset;
import com.antsol.op.asset.Company;
import com.antsol.op.currency.Currency;
import com.antsol.op.investor.product.Buyable;
import com.antsol.op.investor.product.BuyableWrapper;
import com.antsol.op.investor.product.Sellable;
import com.antsol.op.investor.product.SellableWrapper;
import com.antsol.op.market.Market;
import com.antsol.op.settings.Settings;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base class for all investors.
 * Enable buying and selling products.
 * Logic of run must be provided by subclass.
 */
@Slf4j
@Getter
public abstract class AbstractInvestor extends Thread {

    protected static final Faker randomDataSupplier = new Faker();

    protected final Currency currency;
    protected final List<Market<? extends Asset>> markets;
    protected final List<Sellable> products;
    protected final Settings settings;
    protected float budget;

    protected AbstractInvestor(float budget, Currency currency,
                               Settings settings, List<Market<? extends Asset>> markets) {
        this.budget = budget;
        this.currency = currency;
        this.markets = markets;
        this.settings = settings;
        this.products = new ArrayList<>();
    }

    @Override
    public synchronized void start() {
        super.start();
        log.info("{} started.", this);
    }

    /**
     * Buy given product.
     */
    public synchronized void buy(final BuyableWrapper<? extends Buyable> product) {
        products.add(product.getProduct().buy(product.getValue(), product.getPrice()));
        budget -= product.getPrice();
    }

    /**
     * Sell given product.
     */
    public synchronized void sell(final SellableWrapper<? extends Sellable> product) {
        products.remove(product.getProduct());
        product.getProduct().whenSold();
        budget += product.getPrice();
    }

    /**
     * Increases investor budget with given value.
     */
    public synchronized void increaseBudget(final float value) {
        budget += value;
    }

    protected Optional<BuyableWrapper<?>> generateBuy(final List<Buyable> productsToBuy) {
        if (productsToBuy.isEmpty()) return Optional.empty();
        final List<Pair<Buyable, Double>> products = productsToBuy.stream()
                .filter(x -> x.getCurrentBuyPrice(1) != 0)
                .map(x -> new Pair<>(x, x.getAttractiveness()))
                .collect(Collectors.toList());
        if (products.isEmpty()) return Optional.empty();
        final Buyable product = new EnumeratedDistribution<>(products).sample();
        final float price = product.getCurrentBuyPrice(1);
        final float buyValue = ((float) Math.random() * (0.5f * product.getMaxValueForPrice(budget, price)));
        if (product instanceof Company && (!((Company) product).isSellingStocks() || buyValue < 1)) return Optional.empty();
        return Optional.of(new BuyableWrapper<>(product, buyValue, buyValue * price));
    }

    protected Optional<SellableWrapper<?>> generateSell() {
        if (products.isEmpty()) return Optional.empty();
        final List<Pair<Sellable, Double>> toSell = products.stream()
                .map(x -> new Pair<>(x, x.getAttractiveness()))
                .collect(Collectors.toList());
        final Sellable product = new EnumeratedDistribution<>(toSell).sample();
        final float price = product.getCurrentSellPrice();
        return Optional.of(new SellableWrapper<>(product, price));
    }

    protected void doRandomAssetBuy() {
        List<Buyable> assets;
        synchronized (new Object()) {
            assets = markets.stream()
                    .filter(x -> x.getCurrency().equals(currency))
                    .flatMap(x -> x.getAssetsList().stream())
                    .collect(Collectors.toList());
        }
        generateBuy(assets).ifPresent(this::buy);
    }

    protected void doRandomSell() {
        generateSell().ifPresent(this::sell);
    }

}
