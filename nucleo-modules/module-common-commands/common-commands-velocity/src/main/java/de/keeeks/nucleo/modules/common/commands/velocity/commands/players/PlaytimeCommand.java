package de.keeeks.nucleo.modules.common.commands.velocity.commands.players;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionGroup;
import de.keeeks.lejet.api.permission.PermissionUser;
import de.keeeks.lejet.api.permission.PrefixType;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Command({"playtime", "pt"})
public class PlaytimeCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final PermissionApi permissionApi = PermissionApi.instance();

    @DefaultFor({"playtime", "pt"})
    public void commandUsage(
            Player player,
            @Optional String otherPlayerName
    ) {
        Scheduler.runAsync(() -> {
            if (otherPlayerName == null) {
                Long playTime = playerService.onlinePlayer(player.getUniqueId()).map(
                        NucleoPlayer::onlineTime
                ).orElse(0L);
                player.sendMessage(translatable(
                        "nucleo.commands.playtime.self",
                        text(Formatter.formatTime(playTime, false))
                ));
                return;
            }

            playerService.player(otherPlayerName).ifPresentOrElse(nucleoPlayer -> {
                long onlineTime = nucleoPlayer.onlineTime();
                PermissionGroup playerGroup = permissionApi.user(nucleoPlayer.uuid()).flatMap(
                        PermissionUser::highestPermissionGroup
                ).orElse(permissionApi.defaultGroup().orElseThrow());
                player.sendMessage(translatable(
                        "nucleo.commands.playtime.other",
                        playerGroup.prefix(PrefixType.DISPLAY),
                        text(otherPlayerName),
                        text(Formatter.formatTime(onlineTime, false))
                ));
            }, () -> player.sendMessage(translatable("playerNotFound", text(otherPlayerName))));
        });
    }
}