package de.keeeks.nucleo.modules.economy.api.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;

import java.util.UUID;

public class EconomyUserResetPacket extends EconomyUserUpdatePacket{
    public EconomyUserResetPacket(Economy economy, UUID uuid) {
        super(economy, uuid);
    }
}