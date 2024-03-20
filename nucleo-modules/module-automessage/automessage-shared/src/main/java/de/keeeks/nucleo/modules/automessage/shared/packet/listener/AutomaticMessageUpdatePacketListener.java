package de.keeeks.nucleo.modules.automessage.shared.packet.listener;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageUpdatePacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import io.nats.client.Message;

import java.util.logging.Logger;

@ListenerChannel(AutomaticMessageApi.CHANNEL)
public class AutomaticMessageUpdatePacketListener extends AutomaticMessagePacketListener<AutomaticMessageUpdatePacket> {
    public AutomaticMessageUpdatePacketListener(
            NucleoAutomaticMessageApi messageApi,
            Logger logger
    ) {
        super(AutomaticMessageUpdatePacket.class, messageApi, logger);
    }

    @Override
    public void receive(
            AutomaticMessageUpdatePacket automaticMessageUpdatePacket,
            Message message
    ) {
        messageApi.modifyMessages(list -> {
            AutomaticMessage automaticMessage = automaticMessageUpdatePacket.automaticMessage();
            list.remove(automaticMessage);
            list.add(automaticMessage);
        });
    }
}