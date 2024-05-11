package de.keeeks.nucleo.modules.economy.api;

import java.util.List;
import java.util.UUID;

public interface Economy {

    int id();

    String name();

    void modify(UUID uuid, EconomyBalanceModifier modifier);

    /**
     * Returns the top 10 players of this economy.
     * @return The top 10 players
     */
    List<UUID> top();

    double balance(UUID uuid);

    void deposit(UUID uuid, double amount);

    void withdraw(UUID uuid, double amount);

    void transfer(UUID from, UUID to, double amount);

    void setBalance(UUID uuid, double amount);

    void delete();

}