package de.keeeks.nucleo.modules.players.spigot.packet.listener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.packet.player.NucleoOnlinePlayerUpdatePacket;
import de.keeeks.nucleo.modules.players.shared.updater.PlayerLocaleUpdater;
import io.nats.client.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoSpigotOnlinePlayerUpdatePacketListener extends PacketListener<NucleoOnlinePlayerUpdatePacket> {
    private final PlayerLocaleUpdater playerLocaleUpdater = ServiceRegistry.service(
            PlayerLocaleUpdater.class
    );

    private final Module module;

    public NucleoSpigotOnlinePlayerUpdatePacketListener(Module module) {
        super(NucleoOnlinePlayerUpdatePacket.class);
        this.module = module;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerUpdatePacket nucleoOnlinePlayerUpdatePacket,
            Message message
    ) {

        Player player = Bukkit.getPlayer(nucleoOnlinePlayerUpdatePacket.nucleoOnlinePlayer().uuid());
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