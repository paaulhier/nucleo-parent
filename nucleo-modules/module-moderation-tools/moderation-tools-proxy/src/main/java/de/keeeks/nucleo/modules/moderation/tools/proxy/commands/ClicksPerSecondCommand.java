package de.keeeks.nucleo.modules.moderation.tools.proxy.commands;

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
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bungee.annotation.CommandPermission;

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
            Audience audience,
            @Optional String target
    ) {
        if (target == null) {
            audience.sendMessage(Component.translatable("nucleo.moderation.tools.cps.usage"));
            return;
        }
        UUID uuid = audience.get(Identity.UUID).orElseThrow();
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        playerService.onlinePlayer(target).ifPresentOrElse(nucleoOnlinePlayer -> {
            clickCheckApi.clickCheck(uuid).ifPresentOrElse(
                    clickCheckInformation -> audience.sendMessage(Component.translatable(
                            "nucleo.moderation.tools.cps.activate.alreadyActive"
                    )), () -> createClickCheck(audience, nucleoOnlinePlayer, uuid, player)
            );
        }, () -> audience.sendMessage(Component.translatable("playerNotOnline")));
    }

    private void createClickCheck(Audience audience, NucleoOnlinePlayer nucleoOnlinePlayer, UUID uuid, ProxiedPlayer player) {
        clickCheckApi.createClickCheck(
                uuid,
                nucleoOnlinePlayer.uuid()
        ).ifPresent(clickCheckInformation -> {
            audience.sendMessage(Component.translatable(
                    "nucleo.moderation.tools.cps.activate.success",
                    Component.text(nucleoOnlinePlayer.name())
            ));

            connectPlayerToServerIfNecessary(audience, nucleoOnlinePlayer, player);
        });
    }

    private static void connectPlayerToServerIfNecessary(
            Audience audience,
            NucleoOnlinePlayer targetPlayer,
            ProxiedPlayer player
    ) {
        if (player != null) {
            ServerInfo info = player.getServer().getInfo();

            if (!info.getName().equals(targetPlayer.server())) {
                connectPlayerToServer(audience, player, info, targetPlayer.name());
            }
        }
    }

    private static void connectPlayerToServer(
            Audience audience,
            ProxiedPlayer player,
            ServerInfo info,
            String targetPlayerName
    ) {
        player.connect(info, (aBoolean, throwable) -> {
            if (throwable != null) {
                audience.sendMessage(Component.translatable(
                        "nucleo.moderation.tools.cps.activate.serverChangeError"
                ));
                return;
            }
            audience.sendMessage(Component.translatable(
                    "nucleo.moderation.tools.cps.activate.serverChangeSuccess",
                    Component.text(targetPlayerName),
                    Component.text(info.getName())
            ));
        });
    }

    @Subcommand({"off", "disable", "deactivate"})
    public void disableCommand(
            Audience audience
    ) {
        UUID uuid = audience.get(Identity.UUID).orElseThrow();
        clickCheckApi.clickCheck(uuid).ifPresentOrElse(
                clickCheckInformation -> {
                    clickCheckApi.removeClickCheck(clickCheckInformation);
                    audience.sendMessage(Component.translatable(
                            "nucleo.moderation.tools.cps.deactivate.success"
                    ));
                },
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.moderation.tools.cps.deactivate.noActive"
                ))
        );
    }

    @Subcommand({"info", "status"})
    public void infoCommand(
            Audience audience
    ) {
        UUID uuid = audience.get(Identity.UUID).orElseThrow();

        clickCheckApi.clickCheck(uuid).ifPresentOrElse(
                clickCheckInformation -> Scheduler.runAsync(() -> {
                    playerService.onlinePlayer(clickCheckInformation.target()).ifPresentOrElse(
                            nucleoOnlinePlayer -> sendDetailedInformation(
                                    audience,
                                    clickCheckInformation,
                                    nucleoOnlinePlayer
                            ),
                            () -> {
                                playerService.player(clickCheckInformation.target()).ifPresent(
                                        nucleoPlayer -> deleteIfTargetIsOffline(
                                                audience,
                                                clickCheckInformation,
                                                nucleoPlayer.name()
                                        )
                                );
                            });
                }),
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.moderation.tools.cps.info.noActive"
                ))
        );
    }

    private void sendDetailedInformation(
            Audience audience,
            ClickCheckInformation clickCheckInformation,
            NucleoOnlinePlayer targetPlayer
    ) {
        audience.sendMessage(Component.translatable(
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
            Audience audience,
            ClickCheckInformation clickCheckInformation,
            String targetName
    ) {
        audience.sendMessage(Component.translatable(
                "nucleo.moderation.tools.cps.info.activeOffline",
                Component.text(targetName)
        ));
        clickCheckApi.removeClickCheck(clickCheckInformation);
    }
}