package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.pagination.PaginationResult;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Command({"alts"})
@CommandPermission("nucleo.commands.alts")
public class AltsCommand {

    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    @Usage("nucleo.commands.alts.usage")
    @DefaultFor("alts")
    public void altsCommand(
            Player player,
            NucleoPlayer nucleoPlayer,
            @Default("1") int page
    ) {
        if (nucleoPlayer == null) {
            player.sendMessage(translatable("playerNotFound"));
            return;
        }

        List<NucleoPlayer> players = playerService.players(nucleoPlayer.lastIpAddress()).stream().filter(
                alt -> !alt.uuid().equals(nucleoPlayer.uuid())
        ).toList();

        if (players.isEmpty()) {
            player.sendMessage(translatable(
                    "nucleo.commands.alts.noAltsFound",
                    NameColorizer.coloredName(nucleoPlayer.uuid())
            ));
            return;
        }

        PaginationResult<NucleoPlayer> paginatedPlayers = PaginationResult.create(
                players,
                page
        );


        player.sendMessage(translatable(
                "nucleo.commands.alts.header",
                NameColorizer.coloredName(nucleoPlayer.uuid()),
                text(paginatedPlayers.page()),
                text(paginatedPlayers.totalPages()),
                text(paginatedPlayers.totalAmount())
        ));
        for (NucleoPlayer alternativeAccount : paginatedPlayers) {
            player.sendMessage(translatable(
                    "nucleo.commands.alts.entry",
                    NameColorizer.coloredName(alternativeAccount.uuid())
            ));
        }
    }
}