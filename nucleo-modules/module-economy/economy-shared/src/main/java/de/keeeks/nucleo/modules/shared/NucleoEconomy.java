package de.keeeks.nucleo.modules.shared;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyBalanceModifier;
import de.keeeks.nucleo.modules.shared.sql.EconomyRepository;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class NucleoEconomy implements Economy {
    private final EconomyRepository economyRepository;
    private final LoadingCache<UUID, Double> balances;
    private final int id;
    private final String name;

    public NucleoEconomy(int id, String name, EconomyRepository economyRepository) {
        this.id = id;
        this.name = name;
        this.economyRepository = economyRepository;

        this.balances = CacheBuilder.newBuilder().build(
                CacheLoader.from(uuid -> economyRepository.balance(id, uuid))
        );
    }

    @Override
    public void modify(UUID uuid, EconomyBalanceModifier modifier) {
        double newBalance = modifier.modifyBalance(balance(uuid));
        balances.put(uuid, newBalance);
    }

    @Override
    public double balance(UUID uuid) {
        return balances.getUnchecked(uuid);
    }

    @Override
    public void deposit(UUID uuid, double amount) {
        modify(uuid, currentBalance -> currentBalance + amount);
        economyRepository.updateBalance(
                id,
                uuid,
                balance(uuid)
        );
    }

    @Override
    public void withdraw(UUID uuid, double amount) {
        modify(uuid, currentBalance -> currentBalance - amount);
        economyRepository.updateBalance(
                id,
                uuid,
                balance(uuid)
        );
    }

    @Override
    public void transfer(UUID from, UUID to, double amount) {
        withdraw(from, amount);
        deposit(to, amount);
    }

    @Override
    public void setBalance(UUID uuid, double amount) {
        modify(uuid, currentBalance -> amount);
        economyRepository.updateBalance(
                id,
                uuid,
                amount
        );
    }

    @Override
    public void delete() {
        economyRepository.deleteEconomy(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NucleoEconomy that = (NucleoEconomy) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}