package de.keeeks.nucleo.core.velocity.command;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.DefaultExceptionHandler;
import revxrsal.commands.exception.MissingArgumentException;

@RequiredArgsConstructor
public class NucleoVelocityExceptionHandler extends DefaultExceptionHandler {
    private final ProxyServer proxyServer;

    @Override
    public void missingArgument(@NotNull CommandActor actor, @NotNull MissingArgumentException exception) {
        String usage = exception.getCommand().getUsage();
        proxyServer.getPlayer(actor.getUniqueId()).ifPresent(
                player -> player.sendMessage(Component.translatable(
                        "nucleo.command.missing-argument",
                        Component.text(usage)
                ))
        );
    }
}