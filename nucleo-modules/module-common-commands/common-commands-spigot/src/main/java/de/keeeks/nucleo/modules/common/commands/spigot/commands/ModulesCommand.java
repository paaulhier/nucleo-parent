package de.keeeks.nucleo.modules.common.commands.spigot.commands;

import de.keeeks.nucleo.core.api.Module;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;

public class ModulesCommand {

    @Command({"modules", "module"})
    @CommandPermission("nucleo.commands.modules")
    public void modulesCommand(
            final BukkitCommandActor actor
    ) {
        List<Module> modules = Module.modules();

        actor.audience().sendMessage(Component.text("Modules (%s): ".formatted(
                modules.size()
        )).append(Module.modulesAsComponent()));
    }
}