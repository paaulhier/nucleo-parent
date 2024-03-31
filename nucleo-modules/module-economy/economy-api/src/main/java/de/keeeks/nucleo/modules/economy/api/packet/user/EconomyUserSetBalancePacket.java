package de.keeeks.nucleo.modules.economy.api.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;

import java.util.UUID;

public class EconomyUserSetBalancePacket extends EconomyUserUpdateBalancePacket{
    public EconomyUserSetBalancePacket(Economy economy, UUID uuid, double balance) {
        super(economy, uuid, balance);
    }
}