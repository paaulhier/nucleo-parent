package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.karistus.api.Punishment;
import de.keeeks.karistus.api.PunishmentApi;
import de.keeeks.karistus.api.PunishmentType;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.pagination.PaginationResult;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import revxrsal.commands.annotation.*;
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
    private final PunishmentApi punishmentApi = ServiceRegistry.service(
            PunishmentApi.class
    );

    @Usage("nucleo.commands.alts.usage")
    @AutoComplete("@players")
    @DefaultFor("~")
    public void altsCommand(
            Player player,
            NucleoPlayer nucleoPlayer,
            @Default("1") int page
    ) {
        if (nucleoPlayer == null) {
            player.sendMessage(translatable("playerNotFound"));
            return;
        }

        List<NucleoPlayer> players = playerService.players(nucleoPlayer.lastIpAddress());

        if (players.isEmpty()) {
            player.sendMessage(translatable(
                    "nucleo.commands.alts.noAltsFound",
                    NameColorizer.coloredName(nucleoPlayer.uuid())
            ));
            return;
        }

        PaginationResult<NucleoPlayer> paginatedPlayers = PaginationResult.create(
                players,
                page,
                5
        );

        player.sendMessage(translatable(
                "nucleo.commands.alts.header",
                NameColorizer.coloredName(nucleoPlayer.uuid()),
                text(paginatedPlayers.page()),
                text(paginatedPlayers.totalPages()),
                text(paginatedPlayers.totalAmount())
        ));

        for (NucleoPlayer alternativeAccount : paginatedPlayers) {
            Component banReason = punishmentApi.activePunishment(
                    alternativeAccount.uuid(),
                    PunishmentType.BAN
            ).map(Punishment::reason).orElse(translatable("nucleo.commands.alts.notBanned"));

            Component muteReason = punishmentApi.activePunishment(
                    alternativeAccount.uuid(),
                    PunishmentType.MUTE
            ).map(Punishment::reason).orElse(translatable("nucleo.commands.alts.notMuted"));

            player.sendMessage(translatable(
                    "nucleo.commands.alts.entry",
                    NameColorizer.coloredName(alternativeAccount.uuid()).clickEvent(ClickEvent.runCommand(
                            "/pi " + alternativeAccount.name()
                    )),
                    text(alternativeAccount.name()),
                    alternativeAccount instanceof NucleoOnlinePlayer
                            ? translatable("nucleo.commands.alts.online")
                            : translatable("nucleo.commands.alts.offline"),
                    banReason,
                    muteReason,
                    translatable("nucleo.commands.alts.punishForBanBypass").clickEvent(ClickEvent.runCommand(
                            "/ban " + alternativeAccount.name() + " banbypass"
                    )),
                    translatable("nucleo.commands.alts.punishForMuteBypass").clickEvent(ClickEvent.runCommand(
                            "/ban " + alternativeAccount.name() + " mutebypass"
                    )),
                    translatable("nucleo.commands.alts.punishForAltAccount").clickEvent(ClickEvent.runCommand(
                            "/ban " + alternativeAccount.name() + " altaccount"
                    ))
            ));
        }
    }
}