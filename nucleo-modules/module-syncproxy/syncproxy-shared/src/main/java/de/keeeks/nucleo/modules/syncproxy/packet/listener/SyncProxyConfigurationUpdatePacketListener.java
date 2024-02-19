package de.keeeks.nucleo.modules.syncproxy.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.syncproxy.packet.SyncProxyConfigurationUpdatePacket;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import io.nats.client.Message;

@ListenerChannel(SyncProxyService.CHANNEL)
public class SyncProxyConfigurationUpdatePacketListener extends PacketListener<SyncProxyConfigurationUpdatePacket> {
    private final SyncProxyService syncProxyService;

    public SyncProxyConfigurationUpdatePacketListener(SyncProxyService syncProxyService) {
        super(SyncProxyConfigurationUpdatePacket.class);
        this.syncProxyService = syncProxyService;
    }

    @Override
    public void receive(
            SyncProxyConfigurationUpdatePacket syncProxyConfigurationUpdatePacket,
            Message message
    ) {
        syncProxyService.updateConfiguration(syncProxyConfigurationUpdatePacket.syncProxyConfiguration());
    }
}