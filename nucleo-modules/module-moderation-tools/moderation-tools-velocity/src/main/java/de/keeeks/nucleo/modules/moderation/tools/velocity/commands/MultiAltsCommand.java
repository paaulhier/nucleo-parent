package de.keeeks.nucleo.modules.moderation.tools.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.karistus.api.PunishmentApi;
import de.keeeks.karistus.api.PunishmentType;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.pagination.PaginationResult;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.translatable;

@Command("mutltialts")
@CommandPermission("nucleo.commands.moderation.multialts")
@Cooldown(value = 10, unit = TimeUnit.MINUTES)
public class MultiAltsCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final PunishmentApi punishmentApi = ServiceRegistry.service(PunishmentApi.class);

    @DefaultFor("multialts")
    public void multiAltsCommand(Player player, @Optional @Default("1") int page) {
        List<PlayerWithAlts> playerWithAltsCollection = playerService.onlinePlayers().stream().map(
                onlinePlayer -> new PlayerWithAlts(
                        onlinePlayer,
                        playerService.players(onlinePlayer.ipAddress())
                )
        ).filter(
                nucleoPlayers -> nucleoPlayers.alts().size() > 1
        ).filter(
                playerWithAlts -> playerWithAlts.alts().stream().anyMatch(this::isPunished)
        ).toList();
        if (playerWithAltsCollection.isEmpty()) {
            player.sendMessage(translatable("nucleo.commands.moderation.multialts.noAlts"));
            return;
        }

        PaginationResult<PlayerWithAlts> paginationResult = PaginationResult.create(
                playerWithAltsCollection,
                page
        );

        for (PlayerWithAlts playerWithAlts : paginationResult) {
            player.sendMessage(translatable(
                    "nucleo.commands.moderation.multialts.player",
                    NameColorizer.coloredName(playerWithAlts.player().uuid())
            ));
            for (NucleoPlayer alt : playerWithAlts.alts()) {
                if (alt.equals(playerWithAlts.player())) continue;
                player.sendMessage(translatable(
                        "nucleo.commands.moderation.multialts.alt",
                        NameColorizer.coloredName(alt.uuid())
                ));
            }
        }
    }

    private boolean isPunished(NucleoPlayer nucleoPlayer) {
        return punishmentApi.activePunishment(nucleoPlayer.uuid(), PunishmentType.MUTE).isPresent() ||
                punishmentApi.activePunishment(nucleoPlayer.uuid(), PunishmentType.BAN).isPresent();
    }

    record PlayerWithAlts(NucleoPlayer player, List<NucleoPlayer> alts) {
    }
}