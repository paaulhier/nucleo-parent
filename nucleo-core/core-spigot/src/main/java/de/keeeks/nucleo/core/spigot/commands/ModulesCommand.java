package de.keeeks.nucleo.core.spigot.commands;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModulesCommand {
    private final NucleoSpigotPlugin plugin;

    @Command({"modules", "module"})
    @CommandPermission("nucleo.commands.modules")
    public void modulesCommand(
            final BukkitCommandActor actor
    ) {
        List<Module> modules = Module.modules();

        Component modulesComponent = Component.join(
                JoinConfiguration.commas(true),
                modules.stream().map(
                        module -> Component.text(module.description().name(), Style.style(
                                module.moduleState().failed() ? NamedTextColor.RED : NamedTextColor.GREEN
                        )).hoverEvent(HoverEvent.showText(Component.text(
                                "State: %s".formatted(module.moduleState())
                        )))
                ).collect(Collectors.toList()
        ));

        actor.audience().sendMessage(Component.text("Modules (%s): ".formatted(
                modules.size()
        )).append(modulesComponent));
    }
}