package de.keeeks.nucleo.core.spigot.command;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.DefaultExceptionHandler;
import revxrsal.commands.exception.MissingArgumentException;

@RequiredArgsConstructor
public class NucleoSpigotExceptionHandler extends DefaultExceptionHandler {
    @Override
    public void missingArgument(@NotNull CommandActor actor, @NotNull MissingArgumentException exception) {
        String usage = exception.getCommand().getUsage();
        actor.as(BukkitCommandActor.class).requirePlayer().sendMessage(Component.translatable(
                "nucleo.command.missing-argument",
                Component.text(usage)
        ));
    }
}