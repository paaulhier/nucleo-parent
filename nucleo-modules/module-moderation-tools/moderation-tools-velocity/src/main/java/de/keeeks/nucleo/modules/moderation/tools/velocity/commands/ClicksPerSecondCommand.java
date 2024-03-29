package de.keeeks.nucleo.modules.moderation.tools.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Command({"cps", "clickspersecond", "clicks-per-second"})
@CommandPermission("nucleo.moderation.tools.cps")
public class ClicksPerSecondCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final ClickCheckApi clickCheckApi = ServiceRegistry.service(ClickCheckApi.class);

    @AutoComplete("@players")
    @DefaultFor({"cps", "clickspersecond", "clicks-per-second"})
    public void clicksPerSecondCommand(
            Player player,
            @Optional String target
    ) {
        if (target == null) {
            player.sendMessage(Component.translatable("nucleo.moderation.tools.cps.usage"));
            return;
        }
        playerService.onlinePlayer(target).ifPresentOrElse(nucleoOnlinePlayer -> {
            UUID uniqueId = player.getUniqueId();
            clickCheckApi.clickCheck(uniqueId).ifPresentOrElse(
                    clickCheckInformation -> player.sendMessage(Component.translatable(
                            "nucleo.moderation.tools.cps.activate.alreadyActive"
                    )), () -> createClickCheck(player, nucleoOnlinePlayer, uniqueId)
            );
        }, () -> player.sendMessage(Component.translatable("playerNotOnline")));
    }

    private void createClickCheck(Player player, NucleoOnlinePlayer nucleoOnlinePlayer, UUID uuid) {
        clickCheckApi.createClickCheck(
                uuid,
                nucleoOnlinePlayer.uuid()
        ).ifPresent(clickCheckInformation -> {
            player.sendMessage(Component.translatable(
                    "nucleo.moderation.tools.cps.activate.success",
                    Component.text(nucleoOnlinePlayer.name())
            ));

            connectPlayerToServerIfNecessary(player, nucleoOnlinePlayer);
        });
    }

    private static void connectPlayerToServerIfNecessary(
            Player player,
            NucleoOnlinePlayer targetPlayer
    ) {
        if (player != null) {
            player.getCurrentServer().filter(
                    serverConnection -> !serverConnection.getServerInfo().getName().equals(targetPlayer.server())
            ).map(
                    ServerConnection::getServer
            ).ifPresent(registeredServer -> connectPlayerToServer(
                    player,
                    registeredServer,
                    targetPlayer.name()
            ));
        }
    }

    private static void connectPlayerToServer(
            Player player,
            RegisteredServer registeredServer,
            String targetPlayerName
    ) {
        player.createConnectionRequest(registeredServer).connect().thenAccept(result -> {
            if (result.isSuccessful()) {
                player.sendMessage(Component.translatable(
                        "nucleo.moderation.tools.cps.activate.serverChangeSuccess",
                        Component.text(targetPlayerName),
                        Component.text(registeredServer.getServerInfo().getName())
                ));
            } else {
                player.sendMessage(Component.translatable(
                        "nucleo.moderation.tools.cps.activate.serverChangeError"
                ));
            }
        });
    }

    @Subcommand({"off", "disable", "deactivate"})
    public void disableCommand(Player player) {
        UUID uuid = player.getUniqueId();
        clickCheckApi.clickCheck(uuid).ifPresentOrElse(
                clickCheckInformation -> {
                    clickCheckApi.removeClickCheck(clickCheckInformation);
                    player.sendMessage(Component.translatable(
                            "nucleo.moderation.tools.cps.deactivate.success"
                    ));
                },
                () -> player.sendMessage(Component.translatable(
                        "nucleo.moderation.tools.cps.deactivate.noActive"
                ))
        );
    }

    @Subcommand({"info", "status"})
    public void infoCommand(Player player) {
        UUID uuid = player.getUniqueId();

        clickCheckApi.clickCheck(uuid).ifPresentOrElse(
                clickCheckInformation -> Scheduler.runAsync(() -> {
                    playerService.onlinePlayer(clickCheckInformation.target()).ifPresentOrElse(
                            nucleoOnlinePlayer -> sendDetailedInformation(
                                    player,
                                    clickCheckInformation,
                                    nucleoOnlinePlayer
                            ),
                            () -> {
                                playerService.player(clickCheckInformation.target()).ifPresent(
                                        nucleoPlayer -> deleteIfTargetIsOffline(
                                                player,
                                                clickCheckInformation,
                                                nucleoPlayer.name()
                                        )
                                );
                            });
                }),
                () -> player.sendMessage(Component.translatable(
                        "nucleo.moderation.tools.cps.info.noActive"
                ))
        );
    }

    private void sendDetailedInformation(
            Player player,
            ClickCheckInformation clickCheckInformation,
            NucleoOnlinePlayer targetPlayer
    ) {
        player.sendMessage(Component.translatable(
                "nucleo.moderation.tools.cps.info.active",
                Component.text(targetPlayer.name()),
                Component.text(targetPlayer.server()),
                Component.text(Formatter.formatDateTime(clickCheckInformation.startTimestamp())),
                Component.text(Formatter.formatLongTime(Duration.between(
                        clickCheckInformation.startTimestamp(),
                        Instant.now()
                ).toMillis()))
        ));
    }

    private void deleteIfTargetIsOffline(
            Player player,
            ClickCheckInformation clickCheckInformation,
            String targetName
    ) {
        player.sendMessage(Component.translatable(
                "nucleo.moderation.tools.cps.info.activeOffline",
                Component.text(targetName)
        ));
        clickCheckApi.removeClickCheck(clickCheckInformation);
    }
}