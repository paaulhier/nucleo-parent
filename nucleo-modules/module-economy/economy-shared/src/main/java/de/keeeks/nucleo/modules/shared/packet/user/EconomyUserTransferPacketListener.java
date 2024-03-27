package de.keeeks.nucleo.modules.shared.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserTransferPacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.shared.NucleoEconomyApi;
import io.nats.client.Message;

@ListenerChannel(EconomyApi.CHANNEL)
public class EconomyUserTransferPacketListener extends EconomyUserUpdatePacketListener<EconomyUserTransferPacket> {
    public EconomyUserTransferPacketListener(NucleoEconomyApi economyApi) {
        super(EconomyUserTransferPacket.class, economyApi);
    }

    @Override
    public void receive(
            EconomyUserTransferPacket economyUserTransferPacket,
            Message message
    ) {
        Economy economy = economyUserTransferPacket.economy();

        economyApi.economy(economy.id()).ifPresent(cachedEconomy -> {
            economy.modify(
                    economyUserTransferPacket.uuid(),
                    currentBalance -> currentBalance - economyUserTransferPacket.amount()
            );
            economy.modify(
                    economyUserTransferPacket.to(),
                    currentBalance -> currentBalance + economyUserTransferPacket.amount()
            );
        });
    }
}