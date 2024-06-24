package de.keeeks.nucleo.modules.common.commands.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.core.velocity.NucleoVelocityPlugin;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.velocity.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.translatable;

@Command("uptime")
@CommandPermission("nucleo.command.uptime")
@RequiredArgsConstructor
public class UptimeCommand extends RedirectableCommand {
    private final NucleoVelocityPlugin plugin;

    @DefaultFor("~")
    public void uptime(Player player) {
        player.sendMessage(translatable(
                "nucleo.command.uptime",
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
    public void spigotUptime(Player player) {
        sendMessageOnServer(player, "uptime");
    }
}