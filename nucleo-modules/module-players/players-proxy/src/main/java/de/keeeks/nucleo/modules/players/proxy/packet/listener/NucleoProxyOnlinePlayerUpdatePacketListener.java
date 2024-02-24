package de.keeeks.nucleo.modules.players.proxy.packet.listener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.packet.player.NucleoOnlinePlayerUpdatePacket;
import de.keeeks.nucleo.modules.players.shared.updater.PlayerLocaleUpdater;
import io.nats.client.Message;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoProxyOnlinePlayerUpdatePacketListener extends PacketListener<NucleoOnlinePlayerUpdatePacket> {
    private final PlayerLocaleUpdater playerLocaleUpdater = ServiceRegistry.service(
            PlayerLocaleUpdater.class
    );

    private final Module module;

    public NucleoProxyOnlinePlayerUpdatePacketListener(Module module) {
        super(NucleoOnlinePlayerUpdatePacket.class);
        this.module = module;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerUpdatePacket nucleoOnlinePlayerUpdatePacket,
            Message message
    ) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(
                nucleoOnlinePlayerUpdatePacket.nucleoOnlinePlayer().uuid()
        );

        if (player == null) return;

        playerLocaleUpdater.updateLocale(
                player,
                nucleoOnlinePlayerUpdatePacket.nucleoOnlinePlayer().locale()
        );
        module.logger().info("Updated locale for player %s to %s".formatted(
                player.getName(),
                nucleoOnlinePlayerUpdatePacket.nucleoOnlinePlayer().locale()
        ));
    }
}