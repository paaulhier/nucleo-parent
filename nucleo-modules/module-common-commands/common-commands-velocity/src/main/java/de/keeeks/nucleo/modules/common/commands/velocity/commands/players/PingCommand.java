package de.keeeks.nucleo.modules.common.commands.velocity.commands.players;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.common.commands.velocity.CommonCommandsVelocityModule;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.ping.PlayerPingRequestPacket;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.ping.PlayerPingResponsePacket;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;

@Command({"ping", "latenz", "latency"})
public class PingCommand {
    private final NatsConnection natsConnection = ServiceRegistry.service(
            NatsConnection.class
    );
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );
    private final CommonCommandsVelocityModule module = Module.module(
            CommonCommandsVelocityModule.class
    );

    @Usage("nucleo.commands.ping.usage")
    @DefaultFor("~")
    @AutoComplete("@onlinePlayers")
    public void ping(Player player, @Optional String targetName) {
        if (!player.hasPermission("nucleo.commands.ping.other")) {
            pingSelf(player);
            return;
        }

        if (targetName != null && !targetName.equalsIgnoreCase(player.getUsername())) {
            pingOtherPlayer(
                    player,
                    targetName
            );
            return;
        }
        pingSelf(player);
    }

    private void pingSelf(Player player) {
        player.sendMessage(Component.translatable(
                "commands.ping.self",
                Component.text(player.getPing()))
        );
    }

    private void pingOtherPlayer(Player player, String targetName) {
        module.proxyServer().getPlayer(targetName).ifPresentOrElse(
                targetPlayer -> player.sendMessage(Component.translatable(
                        "commands.ping.other",
                        NameColorizer.coloredName(targetPlayer.getUniqueId()),
                        Component.text(targetPlayer.getPing())
                )),
                () -> playerService.onlinePlayer(targetName).ifPresentOrElse(
                        targetOnlinePlayer -> natsConnection.request(
                                "common-commands",
                                new PlayerPingRequestPacket(
                                        targetOnlinePlayer.uuid()
                                ),
                                PlayerPingResponsePacket.class
                        ).whenCompleteAsync((playerPingResponsePacket, throwable) -> handlePingResponsePacket(
                                player,
                                NameColorizer.coloredName(targetOnlinePlayer.uuid()),
                                playerPingResponsePacket,
                                throwable
                        )),
                        () -> pingSelf(player)
                )
        );
    }

    private static void handlePingResponsePacket(
            Player player,
            Component targetName,
            PlayerPingResponsePacket playerPingResponsePacket,
            Throwable throwable
    ) {
        if (throwable != null) {
            player.sendMessage(Component.translatable(
                    "commands.ping.error",
                    targetName
            ));
            return;
        }
        player.sendMessage(Component.translatable(
                "commands.ping.other",
                targetName,
                Component.text(playerPingResponsePacket.ping())
        ));
    }
}