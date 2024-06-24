package de.keeeks.nucleo.modules.common.commands.spigot.commands;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("uptime")
@CommandPermission("nucleo.command.uptime")
public class UptimeCommand {
    private final NucleoSpigotPlugin plugin;

    public UptimeCommand(NucleoSpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @DefaultFor("uptime")
    public void uptime(final BukkitCommandActor actor) {
        actor.audience().sendMessage(Component.translatable(
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
}