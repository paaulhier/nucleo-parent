package de.keeeks.nucleo.modules.syncproxy.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.syncproxy.packet.SyncProxyConfigurationDeletePacket;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import io.nats.client.Message;

@ListenerChannel(SyncProxyService.CHANNEL)
public class SyncProxyConfigurationDeletePacketListener extends PacketListener<SyncProxyConfigurationDeletePacket> {
    private final SyncProxyService syncProxyService;

    public SyncProxyConfigurationDeletePacketListener(SyncProxyService syncProxyService) {
        super(SyncProxyConfigurationDeletePacket.class);
        this.syncProxyService = syncProxyService;
    }

    @Override
    public void receive(
            SyncProxyConfigurationDeletePacket syncProxyConfigurationDeletePacket,
            Message message
    ) {
        syncProxyService.deleteLocal(syncProxyConfigurationDeletePacket.configurationId());
    }
}