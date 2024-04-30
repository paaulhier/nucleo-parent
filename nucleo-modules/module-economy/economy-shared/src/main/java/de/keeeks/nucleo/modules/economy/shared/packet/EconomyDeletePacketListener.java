package de.keeeks.nucleo.modules.economy.shared.packet;

import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.api.packet.EconomyDeletePacket;
import de.keeeks.nucleo.modules.economy.shared.NucleoEconomyApi;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import io.nats.client.Message;

@ListenerChannel(EconomyApi.CHANNEL)
public class EconomyDeletePacketListener extends EconomyUpdatePacketListener<EconomyDeletePacket> {
    public EconomyDeletePacketListener(NucleoEconomyApi economyApi) {
        super(EconomyDeletePacket.class, economyApi);
    }

    @Override
    public void receive(EconomyDeletePacket economyDeletePacket, Message message) {
        economyApi.modifyEconomies(list -> list.remove(economyDeletePacket.economy()));
    }
}