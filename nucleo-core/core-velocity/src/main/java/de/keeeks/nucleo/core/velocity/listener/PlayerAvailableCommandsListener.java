package de.keeeks.nucleo.core.velocity.listener;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class PlayerAvailableCommandsListener {

    private final Logger logger;

    @Subscribe
    public void handleAvailableCommands(PlayerAvailableCommandsEvent event) {
        Player player = event.getPlayer();
        RootCommandNode<?> rootNode = event.getRootNode();

        for (CommandNode<?> commandNode : List.copyOf(rootNode.getChildren())) {
            CommandNode<CommandSource> node = (CommandNode<CommandSource>) commandNode;

            if (!node.canUse(player)) {
                rootNode.removeChildByName(node.getName());
            }
        }
    }
}