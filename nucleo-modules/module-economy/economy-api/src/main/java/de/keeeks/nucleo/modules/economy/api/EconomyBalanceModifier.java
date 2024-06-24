package de.keeeks.nucleo.modules.economy.api;

public interface EconomyBalanceModifier {

    /**
     * Modifies the current balance.
     *
     * @param currentBalance The current balance
     * @return The modified balance
     */
    double modifyBalance(double currentBalance);
}