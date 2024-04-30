package de.keeeks.nucleo.modules.economy.shared.packet;

import de.keeeks.nucleo.modules.economy.api.packet.EconomyPacket;
import de.keeeks.nucleo.modules.economy.shared.NucleoEconomyApi;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;

public abstract class EconomyUpdatePacketListener<P extends EconomyPacket> extends PacketListener<P> {
    protected final NucleoEconomyApi economyApi;

    public EconomyUpdatePacketListener(Class<P> packetClass, NucleoEconomyApi economyApi) {
        super(packetClass);
        this.economyApi = economyApi;
    }
}