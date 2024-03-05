package de.keeeks.nucleo.modules.syncproxy.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.syncproxy.packet.SyncProxyReloadPacket;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import io.nats.client.Message;

@ListenerChannel(SyncProxyService.CHANNEL)
public class SyncProxyReloadPacketListener extends PacketListener<SyncProxyReloadPacket> {
    private final SyncProxyService syncProxyService;

    public SyncProxyReloadPacketListener(SyncProxyService syncProxyService) {
        super(SyncProxyReloadPacket.class);
        this.syncProxyService = syncProxyService;
    }

    @Override
    public void receive(
            SyncProxyReloadPacket syncProxyReloadPacket,
            Message message
    ) {
        syncProxyService.reload();
    }
}