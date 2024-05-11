package de.keeeks.nucleo.modules.economy.shared;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.api.EconomyBalanceModifier;
import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserDepositPacket;
import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserSetBalancePacket;
import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserTransferPacket;
import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserWithdrawPacket;
import de.keeeks.nucleo.modules.economy.shared.sql.EconomyRepository;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class NucleoEconomy implements Economy {
    private static final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    private final transient List<UUID> tops = new ArrayList<>();

    private final transient EconomyRepository economyRepository;
    private final transient LoadingCache<UUID, Double> balances;
    private final int id;
    private final String name;

    public NucleoEconomy(int id, String name, EconomyRepository economyRepository) {
        this.id = id;
        this.name = name;
        this.economyRepository = economyRepository;

        this.balances = CacheBuilder.newBuilder().build(
                CacheLoader.from(uuid -> economyRepository.balance(id, uuid))
        );

        Scheduler.runAsyncTimer(
                () -> {
                    tops.clear();
                    tops.addAll(economyRepository.top(id, 10));
                },
                0,
                30,
                TimeUnit.MINUTES
        );
    }

    @Override
    public void modify(UUID uuid, EconomyBalanceModifier modifier) {
        double newBalance = modifier.modifyBalance(balance(uuid));
        balances.put(uuid, newBalance);
    }

    @Override
    public List<UUID> top() {
        return List.copyOf(tops);
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
        publishPacket(new EconomyUserDepositPacket(
                this,
                uuid,
                amount
        ));
    }

    @Override
    public void withdraw(UUID uuid, double amount) {
        modify(uuid, currentBalance -> currentBalance - amount);
        economyRepository.updateBalance(
                id,
                uuid,
                balance(uuid)
        );
        publishPacket(new EconomyUserWithdrawPacket(
                this,
                uuid,
                amount
        ));
    }

    @Override
    public void transfer(UUID from, UUID to, double amount) {
        withdraw(from, amount);
        deposit(to, amount);
        publishPacket(new EconomyUserTransferPacket(
                this,
                from,
                amount,
                to
        ));
    }

    @Override
    public void setBalance(UUID uuid, double amount) {
        modify(uuid, currentBalance -> amount);
        economyRepository.updateBalance(
                id,
                uuid,
                amount
        );
        publishPacket(new EconomyUserSetBalancePacket(
                this,
                uuid,
                amount
        ));
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

    private <P extends Packet> void publishPacket(P packet) {
        natsConnection.publishPacket(
                EconomyApi.CHANNEL,
                packet
        );
    }
}