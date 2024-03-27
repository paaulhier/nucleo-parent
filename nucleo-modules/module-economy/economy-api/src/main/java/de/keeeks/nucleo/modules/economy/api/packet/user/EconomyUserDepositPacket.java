package de.keeeks.nucleo.modules.economy.api.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import lombok.Getter;

import java.util.UUID;

public class EconomyUserDepositPacket extends EconomyUserUpdateBalancePacket{
    public EconomyUserDepositPacket(Economy economy, UUID uuid, double amount) {
        super(economy, uuid, amount);
    }
}