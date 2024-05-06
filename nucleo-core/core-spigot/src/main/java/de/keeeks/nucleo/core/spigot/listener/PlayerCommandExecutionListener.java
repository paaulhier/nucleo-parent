package de.keeeks.nucleo.core.spigot.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.command.UnknownCommandEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.logging.Logger;

import static net.kyori.adventure.text.Component.translatable;

@RequiredArgsConstructor
public class PlayerCommandExecutionListener implements Listener {
    private final List<DisabledCommandMeta> disabledCommands = List.of(
            DisabledCommandMeta.of("version", "*"),
            DisabledCommandMeta.of("plugins", "nucleo.command.plugins"),
            DisabledCommandMeta.of("pl", "nucleo.command.plugins"),
            DisabledCommandMeta.of("bukkit:plugins", "nucleo.command.plugins"),
            DisabledCommandMeta.of("bukkit:pl", "nucleo.command.plugins"),
            DisabledCommandMeta.of("bukkit:version", "*"),
            DisabledCommandMeta.of("paper", "*")
    );
    private final Logger logger;

    @EventHandler
    public void handleCommand(UnknownCommandEvent event) {
        event.message(translatable("nucleo.command.unknown"));
    }

    @EventHandler
    public void handleCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        logger.info("Player " + player.getName() + " tried to execute command " + message);
        for (DisabledCommandMeta disabledCommand : disabledCommands) {
            if (!message.startsWith(disabledCommand.commandName())) continue;
            if (disabledCommand.requiredPermission() != null) {
                event.setCancelled(!player.hasPermission(disabledCommand.requiredPermission()));
                logger.info("Player " + player.getName() + " tried to execute command " + message + " (cancelled: " + event.isCancelled() + ")");
            } else {
                event.setCancelled(true);
                logger.info("Player " + player.getName() + " tried to execute command " + message + " (cancelled: " + event.isCancelled() + ")");
            }
        }
    }

    record DisabledCommandMeta(String commandName, String requiredPermission) {

        public static DisabledCommandMeta of(String commandName, String requiredPermission) {
            return new DisabledCommandMeta(commandName, requiredPermission);
        }

        public static DisabledCommandMeta of(String commandName) {
            return new DisabledCommandMeta(commandName, null);
        }
    }
}