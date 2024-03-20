package de.keeeks.nucleo.core.proxy.command;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.DefaultExceptionHandler;
import revxrsal.commands.exception.MissingArgumentException;

@RequiredArgsConstructor
public class NucleoProxyExceptionHandler extends DefaultExceptionHandler {
    private final BungeeAudiences audiences;

    @Override
    public void missingArgument(@NotNull CommandActor actor, @NotNull MissingArgumentException exception) {
        String usage = exception.getCommand().getUsage();
        audiences.player(actor.getUniqueId()).sendMessage(Component.translatable(
                "nucleo.command.missing-argument",
                Component.text(usage)
        ));
    }
}