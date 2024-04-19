package de.keeeks.nucleo.core.spigot.command;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.DefaultExceptionHandler;
import revxrsal.commands.exception.MissingArgumentException;
import revxrsal.commands.exception.NoPermissionException;
import revxrsal.commands.exception.SendableException;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@RequiredArgsConstructor
public class NucleoSpigotExceptionHandler extends DefaultExceptionHandler {
    @Override
    public void missingArgument(@NotNull CommandActor actor, @NotNull MissingArgumentException exception) {
        String usage = exception.getCommand().getUsage();
        actor.as(BukkitCommandActor.class).requirePlayer().sendMessage(translatable(
                "nucleo.command.missing-argument",
                Component.text(usage)
        ));
    }

    @Override
    public void noPermission(@NotNull CommandActor actor, @NotNull NoPermissionException exception) {
        actor.as(BukkitCommandActor.class).requirePlayer().sendMessage(translatable("nucleo.command.no-permission"));
    }

    @Override
    public void onUnhandledException(@NotNull CommandActor actor, @NotNull Throwable throwable) {
        actor.as(BukkitCommandActor.class).requirePlayer().sendMessage(translatable(
                "error",
                text(throwable.getMessage())
        ));
    }

    @Override
    public void sendableException(@NotNull CommandActor actor, @NotNull SendableException exception) {
        actor.as(BukkitCommandActor.class).requirePlayer().sendMessage(translatable(
                exception.getMessage()
        ));
    }
}