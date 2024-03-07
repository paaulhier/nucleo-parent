package de.keeeks.nucleo.modules.syncproxy.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import de.keeeks.nucleo.syncproxy.api.packet.SyncProxyReloadPacket;
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