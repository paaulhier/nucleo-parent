package de.keeeks.nucleo.modules.automessage.shared.packet.listener;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageDeletePacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import io.nats.client.Message;

import java.util.logging.Logger;

@ListenerChannel(AutomaticMessageApi.CHANNEL)
public class AutomaticMessageDeletePacketListener extends AutomaticMessagePacketListener<AutomaticMessageDeletePacket> {

    public AutomaticMessageDeletePacketListener(
            NucleoAutomaticMessageApi messageApi,
            Logger logger
    ) {
        super(AutomaticMessageDeletePacket.class, messageApi, logger);
    }

    @Override
    public void receive(AutomaticMessageDeletePacket automaticMessageDeletePacket, Message message) {
        messageApi.modifyMessages(list -> list.remove(automaticMessageDeletePacket.automaticMessage()));
    }
}