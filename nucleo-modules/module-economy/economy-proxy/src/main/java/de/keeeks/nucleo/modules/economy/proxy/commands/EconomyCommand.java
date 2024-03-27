package de.keeeks.nucleo.modules.economy.proxy.commands;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bungee.annotation.CommandPermission;

@Command({"economy", "eco"})
@CommandPermission("nucleo.commands.economy")
public class EconomyCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final EconomyApi economyApi = ServiceRegistry.service(EconomyApi.class);

    @DefaultFor({"economy", "eco"})
    public void economyCommand(Audience audience) {
        audience.sendMessage(Component.translatable(
                "nucleo.economy.command.help"
        ));
    }

    @Subcommand("create")
    @CommandPermission("nucleo.commands.economy.create")
    public void create(
            Audience audience,
            String economyName
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(
                economy -> sendEconomyAlreadyExists(audience, economyName),
                () -> {
                    Economy economy = economyApi.create(economyName);
                    audience.sendMessage(Component.translatable(
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
            Audience audience,
            String economyName
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(economy -> {
            economyApi.delete(economyName);
            audience.sendMessage(Component.translatable(
                    "nucleo.economy.command.economyDeleted",
                    Component.text(economyName)
            ));
        }, () -> sendEconomyNotFound(audience, economyName)));
    }

    private static void sendEconomyAlreadyExists(Audience audience, String economyName) {
        audience.sendMessage(Component.translatable(
                "nucleo.economy.command.economyAlreadyExists",
                Component.text(economyName)
        ));
    }

    @Usage("balance <economy> <player>")
    @AutoComplete("@economies @players")
    @Subcommand("balance")
    public void balance(
            Audience audience,
            String economyName,
            String playerName
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(economy -> {
            playerService.player(playerName).ifPresentOrElse(player -> {
                double balance = economy.balance(player.uuid());

                audience.sendMessage(Component.translatable(
                        "nucleo.economy.command.balance",
                        Component.text(playerName),
                        Component.text(balance),
                        Component.text(economy.name())
                ));
            }, () -> sendPlayerNotFound(audience, playerName));
        }, () -> sendEconomyNotFound(audience, economyName)));
    }

    @Usage("set <economy> <player> <amount>")
    @AutoComplete("@economies @players")
    @Subcommand("set")
    @CommandPermission("nucleo.commands.economy.set")
    public void set(
            Audience audience,
            String economyName,
            String playerName,
            double amount
    ) {
        Scheduler.runAsync(() -> economyApi.economy(economyName).ifPresentOrElse(economy -> {
            playerService.player(playerName).ifPresentOrElse(player -> {
                economy.setBalance(player.uuid(), amount);

                audience.sendMessage(Component.translatable(
                        "nucleo.economy.command.set",
                        Component.text(playerName),
                        Component.text(amount),
                        Component.text(economy.name())
                ));
            }, () -> sendPlayerNotFound(audience, playerName));
        }, () -> sendEconomyNotFound(audience, economyName)));
    }

    private void sendPlayerNotFound(Audience audience, String playerName) {
        audience.sendMessage(Component.translatable(
                "playerNotFound",
                Component.text(playerName)
        ));
    }

    private void sendEconomyNotFound(Audience audience, String economyName) {
        audience.sendMessage(Component.translatable(
                "nucleo.economy.command.economyNotFound",
                Component.text(economyName)
        ));
    }

}