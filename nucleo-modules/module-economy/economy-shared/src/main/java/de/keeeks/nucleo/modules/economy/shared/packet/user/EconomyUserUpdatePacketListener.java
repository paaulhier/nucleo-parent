package de.keeeks.nucleo.modules.economy.shared.packet.user;

import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserUpdatePacket;
import de.keeeks.nucleo.modules.economy.shared.NucleoEconomyApi;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;

public abstract class EconomyUserUpdatePacketListener<P extends EconomyUserUpdatePacket> extends PacketListener<P> {
    protected final NucleoEconomyApi economyApi;

    public EconomyUserUpdatePacketListener(Class<P> packetClass, NucleoEconomyApi economyApi) {
        super(packetClass);
        this.economyApi = economyApi;
    }
}