package de.keeeks.nucleo.modules.automessage.shared.packet.listener;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageReloadPacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import io.nats.client.Message;

import java.util.logging.Logger;

@ListenerChannel(AutomaticMessageApi.CHANNEL)
public class AutomaticMessageReloadPacketListener extends AutomaticMessagePacketListener<AutomaticMessageReloadPacket> {
    public AutomaticMessageReloadPacketListener(
            NucleoAutomaticMessageApi messageApi,
            Logger logger
    ) {
        super(AutomaticMessageReloadPacket.class, messageApi, logger);
    }

    @Override
    public void receive(
            AutomaticMessageReloadPacket automaticMessageReloadPacket,
            Message message
    ) {
        messageApi.loadMessages();
    }
}