package de.keeeks.nucleo.modules.automessage.shared.packet.listener;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageCreatePacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import io.nats.client.Message;

import java.util.logging.Logger;

@ListenerChannel(AutomaticMessageApi.CHANNEL)
public class AutomaticMessageCreatePacketListener extends AutomaticMessagePacketListener<AutomaticMessageCreatePacket> {

    public AutomaticMessageCreatePacketListener(
            NucleoAutomaticMessageApi messageApi,
            Logger logger
    ) {
        super(AutomaticMessageCreatePacket.class, messageApi, logger);
    }

    @Override
    public void receive(
            AutomaticMessageCreatePacket automaticMessageCreatePacket,
            Message message
    ) {
        messageApi.modifyMessages(list -> list.add(automaticMessageCreatePacket.automaticMessage()));
    }
}