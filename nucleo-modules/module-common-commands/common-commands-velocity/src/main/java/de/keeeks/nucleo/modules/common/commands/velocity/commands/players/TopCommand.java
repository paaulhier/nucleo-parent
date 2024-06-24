package de.keeeks.nucleo.modules.common.commands.velocity.commands.players;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.lejet.api.permission.PrefixType;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Command({"top", "best"})
public class TopCommand {
    private static final NumberFormat cookiesFormat = DecimalFormat.getNumberInstance(Locale.GERMAN);

    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final EconomyApi economyApi = ServiceRegistry.service(EconomyApi.class);
    private final Economy economy = economyApi.economy("cookies").orElseGet(
            () -> economyApi.create("cookies")
    );

    @DefaultFor("~")
    public void topCommand(Player player) {
        player.sendMessage(translatable("nucleo.command.top.usage"));
    }

    @Subcommand("cookies")
    public void cookiesCommand(Player player) {
        Scheduler.runAsync(() -> {
            player.sendMessage(translatable("nucleo.command.top.cookies.header"));

            List<NucleoPlayer> topEconomyPlayers = economy.top().stream().map(
                    uuid -> playerService.player(uuid).orElse(null)
            ).filter(Objects::nonNull).toList();

            int place = 1;
            for (NucleoPlayer nucleoPlayer : topEconomyPlayers) {
                double balance = economy.balance(nucleoPlayer.uuid());
                player.sendMessage(translatable(
                        "nucleo.command.top.cookies.entry",
                        text(nucleoPlayer.name()),
                        NameColorizer.coloredName(nucleoPlayer.uuid()),
                        NameColorizer.nameWithPrefix(nucleoPlayer.uuid(), PrefixType.TAB),
                        text(cookiesFormat.format(balance)),
                        text(place)
                ));
                place++;
            }
        });
    }

    @Subcommand("playtime")
    public void playtimeCommand(Player player) {
        Scheduler.runAsync(() -> {
            player.sendMessage(translatable("nucleo.command.top.playtime.header"));
            int place = 1;
            for (NucleoPlayer nucleoPlayer : playerService.playersSortedByPlayTime()) {
                player.sendMessage(translatable(
                        "nucleo.command.top.playtime.entry",
                        text(nucleoPlayer.name()),
                        NameColorizer.coloredName(nucleoPlayer.uuid()),
                        NameColorizer.nameWithPrefix(nucleoPlayer.uuid(), PrefixType.TAB),
                        text(Formatter.formatLongTime(nucleoPlayer.onlineTime())),
                        text(place)
                ));
                place++;
            }
        });
    }
}