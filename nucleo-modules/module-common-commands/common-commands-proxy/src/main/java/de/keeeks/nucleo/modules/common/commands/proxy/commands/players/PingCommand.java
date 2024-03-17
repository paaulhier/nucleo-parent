package de.keeeks.nucleo.modules.common.commands.proxy.commands.players;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.common.commands.proxy.packet.ping.PlayerPingRequestPacket;
import de.keeeks.nucleo.modules.common.commands.proxy.packet.ping.PlayerPingResponsePacket;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bungee.BungeeCommandActor;

import java.util.logging.Level;

@Command({"ping", "latenz", "latency"})
@RequiredArgsConstructor
public class PingCommand {
    private final NatsConnection natsConnection = ServiceRegistry.service(
            NatsConnection.class
    );
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    private final BungeeAudiences bungeeAudiences;


    @Usage("ping [player]")
    @DefaultFor({"ping", "latenz", "latency"})
    @AutoComplete("@players")
    public void ping(
            BungeeCommandActor actor,
            @Optional String targetName
    ) {
        if (!actor.isPlayer()) return;
        ProxiedPlayer player = actor.asPlayer();
        if (player == null) return;

        Audience audience = bungeeAudiences.player(player);

        if (!player.hasPermission("nucleo.commands.ping.other")) {
            pingSelf(
                    audience,
                    player
            );
            return;
        }

        if (targetName != null && !targetName.equalsIgnoreCase(player.getName())) {
            pingOtherPlayer(
                    audience,
                    player,
                    targetName
            );
            return;
        }
        pingSelf(
                audience,
                player
        );
    }

    private void pingSelf(Audience audience, ProxiedPlayer player) {
        audience.sendMessage(Component.translatable(
                "commands.ping.self",
                Component.text(player.getPing()))
        );
    }

    private void pingOtherPlayer(Audience audience, ProxiedPlayer self, String targetName) {
        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetName);
        if (targetPlayer == null) {
            playerService.onlinePlayer(targetName).ifPresentOrElse(
                    nucleoOnlinePlayer -> natsConnection.request(
                            "common-commands",
                            new PlayerPingRequestPacket(
                                    nucleoOnlinePlayer.uuid()
                            ),
                            PlayerPingResponsePacket.class
                    ).whenCompleteAsync((playerPingResponsePacket, throwable) -> handlePingResponsePacket(
                            audience,
                            nucleoOnlinePlayer.name(),
                            playerPingResponsePacket,
                            throwable
                    )),
                    () -> pingSelf(audience, self)
            );
        } else {
            audience.sendMessage(Component.translatable(
                    "commands.ping.other",
                    Component.text(targetPlayer.getName()),
                    Component.text(targetPlayer.getPing())
            ));
        }
    }

    private static void handlePingResponsePacket(
            Audience audience,
            String targetName,
            PlayerPingResponsePacket playerPingResponsePacket,
            Throwable throwable
    ) {
        if (throwable != null) {
            audience.sendMessage(Component.translatable(
                    "commands.ping.error",
                    Component.text(targetName)
            ));
            ProxyServer.getInstance().getLogger().log(
                    Level.SEVERE,
                    "Error while fetching ping for player " + targetName,
                    throwable
            );
            return;
        }
        audience.sendMessage(Component.translatable(
                "commands.ping.other",
                Component.text(targetName),
                Component.text(playerPingResponsePacket.ping())
        ));
    }
}