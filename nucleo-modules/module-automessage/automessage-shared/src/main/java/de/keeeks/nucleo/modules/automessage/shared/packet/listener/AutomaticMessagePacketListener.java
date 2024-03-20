package de.keeeks.nucleo.modules.automessage.shared.packet.listener;

import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessageApi;
import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;

import java.util.logging.Logger;

public abstract class AutomaticMessagePacketListener<P extends Packet> extends PacketListener<P> {
    protected final NucleoAutomaticMessageApi messageApi;
    protected final Logger logger;

    public AutomaticMessagePacketListener(
            Class<P> packetClass,
            NucleoAutomaticMessageApi messageApi,
            Logger logger
    ) {
        super(packetClass);
        this.messageApi = messageApi;
        this.logger = logger;
    }
}