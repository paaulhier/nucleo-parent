package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.lejet.api.packets.PermissionUserUpdatedPacket;
import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import io.nats.client.Message;

@ListenerChannel(PermissionApi.CHANNEL)
public class TabDecorationPermissionUserUpdatePacketListener extends PacketListener<PermissionUserUpdatedPacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    private final ProxyServer proxyServer;

    public TabDecorationPermissionUserUpdatePacketListener(ProxyServer proxyServer) {
        super(PermissionUserUpdatedPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            PermissionUserUpdatedPacket permissionUserUpdatedPacket,
            Message message
    ) {
        proxyServer.getPlayer(permissionUserUpdatedPacket.permissionUser().uuid()).ifPresent(
                tabDecorationService::sendPlayerListHeaderAndFooter
        );
    }
}