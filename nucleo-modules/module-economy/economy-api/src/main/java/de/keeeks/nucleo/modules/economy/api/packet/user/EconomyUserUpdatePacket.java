package de.keeeks.nucleo.modules.economy.api.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.packet.EconomyPacket;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class EconomyUserUpdatePacket extends EconomyPacket {
    protected final UUID uuid;
    public EconomyUserUpdatePacket(Economy economy, UUID uuid) {
        super(economy);
        this.uuid = uuid;
    }
}