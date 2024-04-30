package de.keeeks.nucleo.modules.translation.shared.packet.user;

import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.api.packet.user.EconomyUserUpdateBalancePacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.translation.shared.NucleoEconomyApi;
import io.nats.client.Message;

@ListenerChannel(EconomyApi.CHANNEL)
public class EconomyUserUpdateBalancePacketListener extends PacketListener<EconomyUserUpdateBalancePacket> {
    private final NucleoEconomyApi economyApi;

    public EconomyUserUpdateBalancePacketListener(NucleoEconomyApi economyApi) {
        super(EconomyUserUpdateBalancePacket.class);
        this.economyApi = economyApi;
    }

    @Override
    public void receive(
            EconomyUserUpdateBalancePacket economyUserUpdateBalancePacket,
            Message message
    ) {
        Economy economy = economyUserUpdateBalancePacket.economy();
        economyApi.economy(economy.id()).ifPresent(
                cachedEconomy -> cachedEconomy.modify(
                        economyUserUpdateBalancePacket.uuid(),
                        currentBalance -> economyUserUpdateBalancePacket.balance()
                )
        );
    }
}