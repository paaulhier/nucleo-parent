package de.keeeks.nucleo.modules.common.commands.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.Module;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.List;

@Command({"modules", "module"})
@CommandPermission("nucleo.commands.modules")
public class ModulesCommand extends RedirectableCommand {

    @DefaultFor("~")
    public void modulesCommand(Player player) {
        List<Module> modules = Module.modules();

        player.sendMessage(Component.text("Modules (%s): ".formatted(
                modules.size()
        )).append(Module.modulesAsComponent()));
    }

    @Subcommand({"spigot", "bukkit"})
    public void modulesSpigotCommand(Player player) {
        sendMessageOnServer(player, "modules");
    }
}