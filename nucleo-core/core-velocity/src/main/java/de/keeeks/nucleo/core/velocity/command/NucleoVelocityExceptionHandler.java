package de.keeeks.nucleo.core.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.DefaultExceptionHandler;
import revxrsal.commands.exception.MissingArgumentException;
import revxrsal.commands.exception.NoPermissionException;
import revxrsal.commands.exception.SendableException;
import revxrsal.commands.velocity.VelocityCommandActor;

import java.util.logging.Level;
import java.util.logging.Logger;

import static net.kyori.adventure.text.Component.*;

@RequiredArgsConstructor
public class NucleoVelocityExceptionHandler extends DefaultExceptionHandler {
    private final ProxyServer proxyServer;
    private final Logger logger;

    @Override
    public void missingArgument(@NotNull CommandActor actor, @NotNull MissingArgumentException exception) {
        String usage = exception.getCommand().getUsage();

        if (usage.contains(".")) {
            CommandSource commandSource = ((VelocityCommandActor) actor).getSource();
            commandSource.sendMessage(Component.translatable(usage));
            return;
        }

        proxyServer.getPlayer(actor.getUniqueId()).ifPresent(
                player -> player.sendMessage(Component.translatable(
                        "nucleo.command.missing-argument",
                        Component.text(usage)
                ))
        );
    }

    @Override
    public void noPermission(@NotNull CommandActor actor, @NotNull NoPermissionException exception) {
        actor.as(VelocityCommandActor.class).requirePlayer().sendMessage(translatable("nucleo.command.no-permission"));
    }

    @Override
    public void onUnhandledException(@NotNull CommandActor actor, @NotNull Throwable throwable) {
        actor.as(VelocityCommandActor.class).requirePlayer().sendMessage(translatable(
                "error",
                throwable.getMessage() == null ? empty() : text(throwable.getMessage())
        ));
        logger.log(
                Level.SEVERE,
                "An error occurred while executing a command",
                throwable
        );
    }

    @Override
    public void sendableException(@NotNull CommandActor actor, @NotNull SendableException exception) {
        actor.as(VelocityCommandActor.class).requirePlayer().sendMessage(translatable(
                exception.getMessage()
        ));
    }
}