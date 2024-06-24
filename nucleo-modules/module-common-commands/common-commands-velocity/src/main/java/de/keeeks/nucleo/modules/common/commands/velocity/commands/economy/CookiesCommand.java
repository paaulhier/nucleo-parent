package de.keeeks.nucleo.modules.common.commands.velocity.commands.economy;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Command({"cookies", "cookie"})
public class CookiesCommand {
    private static final NumberFormat cookiesFormat = new DecimalFormat("00");
    private final EconomyApi economyApi = ServiceRegistry.service(EconomyApi.class);
    private final Economy economy = economyApi.create("cookies");

    @DefaultFor("~")
    public void cookiesCommand(Player player) {
        Scheduler.runAsync(() -> {
            double cookies = economy.balance(player.getUniqueId());
            player.sendMessage(translatable(
                    "nucleo.economy.command.cookies",
                    text(cookiesFormat.format(cookies))
            ));
        });
    }
}