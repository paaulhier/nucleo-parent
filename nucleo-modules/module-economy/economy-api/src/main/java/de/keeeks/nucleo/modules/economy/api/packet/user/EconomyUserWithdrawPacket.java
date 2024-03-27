package de.keeeks.nucleo.modules.economy.api.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import lombok.Getter;

import java.util.UUID;

public class EconomyUserWithdrawPacket extends EconomyUserUpdateBalancePacket {

    public EconomyUserWithdrawPacket(Economy economy, UUID uuid, double amount) {
        super(economy, uuid, amount);
    }
}