package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import de.keeeks.lejet.api.packets.PermissionUserUpdatedPacket;
import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import io.nats.client.Message;
import org.bukkit.Bukkit;

import java.util.Optional;

@ListenerChannel(PermissionApi.CHANNEL)
public final class TabDecorationPermissionUserUpdatePacketListener extends PacketListener<PermissionUserUpdatedPacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    public TabDecorationPermissionUserUpdatePacketListener() {
        super(PermissionUserUpdatedPacket.class);
    }

    @Override
    public void receive(
            PermissionUserUpdatedPacket permissionUserUpdatedPacket,
            Message message
    ) {
        Optional.ofNullable(Bukkit.getPlayer(permissionUserUpdatedPacket.permissionUser().uuid())).ifPresent(
                tabDecorationService::sendPlayerListHeaderAndFooter
        );
    }
}