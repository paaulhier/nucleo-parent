package de.keeeks.nucleo.modules.economy.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

@Command({"economy", "eco"})
@CommandPermission("nucleo.commands.economy")
public class EconomyCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final EconomyApi economyApi = ServiceRegistry.service(EconomyApi.class);

    @DefaultFor({"economy", "eco"})
    public void economyCommand(Player player) {
        player.sendMessage(Component.translatable(
                "nucleo.economy.command.help"
        ));
    }

    @Subcommand("create")
    @CommandPermission("nucleo.commands.economy.create")
    public void create(
            Player player,
            String economyName
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(
                economy -> sendEconomyAlreadyExists(player, economyName),
                () -> {
                    Economy economy = economyApi.create(economyName);
                    player.sendMessage(Component.translatable(
                            "nucleo.economy.command.economyCreated",
                            Component.text(economyName),
                            Component.text(economy.id())
                    ));
                }
        ));
    }

    @Subcommand("delete")
    @CommandPermission("nucleo.commands.economy.delete")
    public void delete(
            Player player,
            String economyName
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(economy -> {
            economyApi.delete(economyName);
            player.sendMessage(Component.translatable(
                    "nucleo.economy.command.economyDeleted",
                    Component.text(economyName)
            ));
        }, () -> sendEconomyNotFound(player, economyName)));
    }

    private static void sendEconomyAlreadyExists(Player player, String economyName) {
        player.sendMessage(Component.translatable(
                "nucleo.economy.command.economyAlreadyExists",
                Component.text(economyName)
        ));
    }

    @Usage("balance <economy> <player>")
    @AutoComplete("@economies @players")
    @Subcommand("balance")
    public void balance(
            Player player,
            String economyName,
            String playerName
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(economy -> {
            playerService.player(playerName).ifPresentOrElse(targetPlayer -> {
                double balance = economy.balance(targetPlayer.uuid());

                player.sendMessage(Component.translatable(
                        "nucleo.economy.command.balance",
                        Component.text(playerName),
                        Component.text(balance),
                        Component.text(economy.name())
                ));
            }, () -> sendPlayerNotFound(player, playerName));
        }, () -> sendEconomyNotFound(player, economyName)));
    }

    @Usage("set <economy> <player> <amount>")
    @AutoComplete("@economies @players")
    @Subcommand("set")
    @CommandPermission("nucleo.commands.economy.set")
    public void set(
            Player player,
            String economyName,
            String playerName,
            double amount
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(economy -> {
            playerService.player(playerName).ifPresentOrElse(targetPlayer -> {
                economy.setBalance(targetPlayer.uuid(), amount);

                player.sendMessage(Component.translatable(
                        "nucleo.economy.command.set",
                        Component.text(playerName),
                        Component.text(amount),
                        Component.text(economy.name())
                ));
            }, () -> sendPlayerNotFound(player, playerName));
        }, () -> sendEconomyNotFound(player, economyName)));
    }

    private void sendPlayerNotFound(Player player, String playerName) {
        player.sendMessage(Component.translatable(
                "playerNotFound",
                Component.text(playerName)
        ));
    }

    private void sendEconomyNotFound(Player player, String economyName) {
        player.sendMessage(Component.translatable(
                "nucleo.economy.command.economyNotFound",
                Component.text(economyName)
        ));
    }

}