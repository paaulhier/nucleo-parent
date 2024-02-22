package de.keeeks.nucleo.modules.translation.spigot.command;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

import java.util.Locale;

public class LanguageCommand {
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );

    @Command({"language", "lang", "languages"})
    public void defaultLanguagesCommand(Player player) {
        playerService.onlinePlayer(player.getUniqueId()).ifPresent(nucleoOnlinePlayer -> {
            Locale locale = nucleoOnlinePlayer.locale();
            player.sendMessage(Component.translatable("language.%s".formatted(
                    locale.toString()
            )));
            player.sendMessage(Component.translatable(
                    "language.currentSelected"
            ).args(Component.translatable("language.%s".formatted(
                    locale.toString()
            ))));
        });
    }
}