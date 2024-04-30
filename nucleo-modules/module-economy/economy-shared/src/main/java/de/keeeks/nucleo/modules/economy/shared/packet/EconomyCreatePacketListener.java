package de.keeeks.nucleo.modules.economy.shared.packet;

import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.api.packet.EconomyCreatePacket;
import de.keeeks.nucleo.modules.economy.shared.NucleoEconomyApi;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import io.nats.client.Message;

@ListenerChannel(EconomyApi.CHANNEL)
public class EconomyCreatePacketListener extends EconomyUpdatePacketListener<EconomyCreatePacket> {

    public EconomyCreatePacketListener(NucleoEconomyApi economyApi) {
        super(EconomyCreatePacket.class, economyApi);
    }

    @Override
    public void receive(
            EconomyCreatePacket economyCreatePacket,
            Message message
    ) {
        economyApi.modifyEconomies(list -> list.add(economyCreatePacket.economy()));
    }
}