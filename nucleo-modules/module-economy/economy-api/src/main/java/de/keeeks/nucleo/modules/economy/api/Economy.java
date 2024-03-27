package de.keeeks.nucleo.modules.economy.api;

import java.util.UUID;

public interface Economy {

    int id();

    String name();

    void modify(UUID uuid, EconomyBalanceModifier modifier);

    double balance(UUID uuid);

    void deposit(UUID uuid, double amount);

    void withdraw(UUID uuid, double amount);

    void transfer(UUID from, UUID to, double amount);

    void setBalance(UUID uuid, double amount);

    void delete();

}