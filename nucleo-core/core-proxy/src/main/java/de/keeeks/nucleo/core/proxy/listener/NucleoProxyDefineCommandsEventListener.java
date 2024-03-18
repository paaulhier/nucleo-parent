package de.keeeks.nucleo.core.proxy.listener;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.BungeeCommandHandler;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;

import java.util.HashMap;
import java.util.Map;

public class NucleoProxyDefineCommandsEventListener implements Listener {

    private final BungeeCommandHandler bungeeCommandHandler;

    public NucleoProxyDefineCommandsEventListener(BungeeCommandHandler bungeeCommandHandler) {
        this.bungeeCommandHandler = bungeeCommandHandler;
    }

    @EventHandler
    public void handleProxyDefineCommandsEvent(ProxyDefineCommandsEvent event) {
        Connection receiver = event.getReceiver();

        Map<String, Command> commands = new HashMap<>(event.getCommands());

        if (receiver instanceof ProxiedPlayer player) {
            for (Map.Entry<String, Command> entry : commands.entrySet()) {
                Command command = entry.getValue();

                CommandCategory commandCategory = bungeeCommandHandler.getCategories().values().stream().filter(
                        category -> checkLampCommand(category.getPath(), command)
                ).findFirst().orElse(null);

                ExecutableCommand executableCommand = bungeeCommandHandler.getCommands().values().stream().filter(
                        lampCommand -> checkLampCommand(lampCommand.getPath(), command)
                ).findFirst().orElse(null);

                boolean hasPermission;

                if (executableCommand != null) {
                    hasPermission = executableCommand.hasPermission(BungeeCommandActor.wrap(
                            player,
                            executableCommand.getCommandHandler()
                    ));
                } else if (commandCategory != null) {
                    hasPermission = commandCategory.hasPermission(BungeeCommandActor.wrap(
                            player,
                            commandCategory.getCommandHandler()
                    ));
                } else {
                    hasPermission = command.hasPermission(player);
                }

                if (!hasPermission) {
                    event.getCommands().remove(entry.getKey());
                }
            }
        }
    }

    private static boolean checkLampCommand(CommandPath path, Command command) {
        if (path.isRoot()) {
            return path.getName().equals(command.getName());
        }
        return path.getName().equals(command.getName()) || path.getParent().equals(command.getName());
    }
}