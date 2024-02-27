package de.keeeks.nucleo.core.proxy.commands;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;

@Command("uptime")
@CommandPermission("nucleo.command.uptime")
public class UptimeCommand extends RedirectableCommand {
    private final BungeeAudiences bungeeAudiences;
    private final NucleoProxyPlugin plugin;

    public UptimeCommand(NucleoProxyPlugin plugin) {
        this.bungeeAudiences = plugin.bungeeAudiences();
        this.plugin = plugin;
    }

    @DefaultFor("uptime")
    public void uptime(
            final BungeeCommandActor actor
    ) {
        ProxiedPlayer player = actor.asPlayer();
        if (player == null) return;

        bungeeAudiences.player(player).sendMessage(Component.translatable(
                "nucleo.command.uptime"
        ).arguments(
                Component.text(Module.serviceName()),
                Component.text(Formatter.formatTime(
                        (System.currentTimeMillis() - plugin.startupTime()),
                        false
                )),
                Component.text(Formatter.formatTime(
                        plugin.startupDuration().toMillis(),
                        false
                ))
        ));
    }

    @Subcommand({"spigot", "bukkit"})
    public void spigotUptime(
            final BungeeCommandActor actor
    ) {
        ProxiedPlayer player = actor.asPlayer();
        if (player == null) return;

        sendMessageOnServer(
                player,
                "uptime"
        );
    }
}