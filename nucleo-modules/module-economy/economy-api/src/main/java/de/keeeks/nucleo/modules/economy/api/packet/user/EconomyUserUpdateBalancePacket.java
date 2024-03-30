package de.keeeks.nucleo.modules.economy.api.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class EconomyUserUpdateBalancePacket extends EconomyUserUpdatePacket {
    protected final double balance;

    public EconomyUserUpdateBalancePacket(Economy economy, UUID uuid, double balance) {
        super(economy, uuid);
        this.balance = balance;
    }
}