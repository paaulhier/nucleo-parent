package de.keeeks.nucleo.modules.shared.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserResetPacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.shared.NucleoEconomyApi;
import io.nats.client.Message;

@ListenerChannel(EconomyApi.CHANNEL)
public class EconomyUserResetPacketListener extends EconomyUserUpdatePacketListener<EconomyUserResetPacket> {
    public EconomyUserResetPacketListener(NucleoEconomyApi economyApi) {
        super(EconomyUserResetPacket.class, economyApi);
    }

    @Override
    public void receive(EconomyUserResetPacket economyUserResetPacket, Message message) {
        Economy economy = economyUserResetPacket.economy();
        economyApi.economy(economy.id()).ifPresent(cachedEconomy -> cachedEconomy.modify(
                economyUserResetPacket.uuid(),
                currentBalance -> 0
        ));
    }
}