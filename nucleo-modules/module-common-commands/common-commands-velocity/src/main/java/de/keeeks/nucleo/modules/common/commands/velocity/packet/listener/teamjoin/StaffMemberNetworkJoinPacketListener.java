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
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerNetworkJoinPacket;
import io.nats.client.Message;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@ListenerChannel(PlayerService.CHANNEL)
public class StaffMemberNetworkJoinPacketListener extends PacketListener<NucleoOnlinePlayerNetworkJoinPacket> {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);
    private final Notification notification = notificationApi.createNotification(
            "teamjoin",
            "Notifies staff members about other staff member joins"
    );
    private final PermissionApi permissionApi = PermissionApi.instance();
    private final ProxyServer proxyServer;

    public StaffMemberNetworkJoinPacketListener(ProxyServer proxyServer) {
        super(NucleoOnlinePlayerNetworkJoinPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerNetworkJoinPacket nucleoOnlinePlayerNetworkJoinPacket,
            Message message
    ) {
        permissionApi.user(nucleoOnlinePlayerNetworkJoinPacket.player().uuid()).ifPresent(permissionUser -> {
            if (!permissionUser.hasPermission("keeeks.staff")) {
                return;
            }

            proxyServer.getAllPlayers().stream().filter(
                    player -> player.hasPermission("keeeks.staff")
            ).filter(player -> notificationApi.notificationActive(
                    notification,
                    player.getUniqueId()
            )).forEach(player -> {
                NucleoOnlinePlayer nucleoOnlinePlayer = nucleoOnlinePlayerNetworkJoinPacket.player();
                permissionApi.user(nucleoOnlinePlayer.uuid()).map(
                        PermissionUser::coloredName
                ).ifPresent(coloredUsername -> player.sendMessage(translatable(
                        "nucleo.notifications.teamjoin",
                        coloredUsername,
                        text(nucleoOnlinePlayer.server()),
                        text(nucleoOnlinePlayer.proxy())
                )));
            });
        });
    }
}