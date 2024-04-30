package de.keeeks.nucleo.modules.translation.shared.packet.user;

import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserUpdatePacket;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.translation.shared.NucleoEconomyApi;

public abstract class EconomyUserUpdatePacketListener<P extends EconomyUserUpdatePacket> extends PacketListener<P> {
    protected final NucleoEconomyApi economyApi;

    public EconomyUserUpdatePacketListener(Class<P> packetClass, NucleoEconomyApi economyApi) {
        super(packetClass);
        this.economyApi = economyApi;
    }
}