package de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.teamjoin;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionUser;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerDisconnectPacket;
import io.nats.client.Message;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@ListenerChannel(PlayerService.CHANNEL)
public class StaffMemberNetworkDisconnectPacketListener extends PacketListener<NucleoOnlinePlayerDisconnectPacket> {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);
    private final Notification notification = notificationApi.createNotification(
            "teamjoin",
            "Notifies staff members about other staff member joins"
    );
    private final PermissionApi permissionApi = PermissionApi.instance();
    private final ProxyServer proxyServer;

    public StaffMemberNetworkDisconnectPacketListener(ProxyServer proxyServer) {
        super(NucleoOnlinePlayerDisconnectPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerDisconnectPacket nucleoOnlinePlayerDisconnectPacket,
            Message message
    ) {
        NucleoOnlinePlayer player = nucleoOnlinePlayerDisconnectPacket.player();

        permissionApi.user(player.uuid()).ifPresent(permissionUser -> {
            if (!permissionUser.hasPermission("keeeks.staff")) {
                return;
            }

            proxyServer.getAllPlayers().stream().filter(
                    p -> p.hasPermission("keeeks.staff")
            ).filter(p -> notificationApi.notificationActive(
                    notification,
                    p.getUniqueId()
            )).forEach(p -> permissionApi.user(player.uuid()).map(
                    PermissionUser::coloredName
            ).ifPresent(coloredUsername -> p.sendMessage(translatable(
                    "nucleo.notifications.team.leave",
                    coloredUsername,
                    text(player.server()),
                    text(player.proxy())
            ))));
        });
    }
}