package de.keeeks.nucleo.modules.economy.api.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import lombok.Getter;

import java.util.UUID;

@Getter
public class EconomyUserTransferPacket extends EconomyUserUpdatePacket {
    protected final double amount;
    protected final UUID to;

    public EconomyUserTransferPacket(Economy economy, UUID uuid, double amount, UUID to) {
        super(economy, uuid);
        this.amount = amount;
        this.to = to;
    }
}