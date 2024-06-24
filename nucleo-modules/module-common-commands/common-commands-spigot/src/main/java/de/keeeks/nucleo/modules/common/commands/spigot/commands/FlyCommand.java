package de.keeeks.nucleo.modules.common.commands.spigot.commands;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.common.commands.spigot.CommonCommandsSpigotModule;
import de.keeeks.nucleo.modules.common.commands.spigot.event.PlayerToggleFlyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.translatable;

@Command("fly")
@CommandPermission("nucleo.commands.fly")
public class FlyCommand {
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    @DefaultFor("fly")
    public void flyCommand(Player player, @Optional String targetName) {
        if (targetName == null || targetName.equalsIgnoreCase(player.getName())) {
            toggleFlyFor(player, player);
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(translatable("playerNotFound"));
            return;
        }

        toggleFlyFor(player, target);
    }

    private void toggleFlyFor(Player player, Player target) {
        PlayerToggleFlyEvent playerToggleFlyEvent = new PlayerToggleFlyEvent(
                target,
                player,
                !player.getAllowFlight()
        );

        boolean self = player.equals(target);

        pluginManager.callEvent(playerToggleFlyEvent);
        if (playerToggleFlyEvent.cancelled()) {
            player.sendMessage(translatable("commands.fly.disabledForService"));
            return;
        }
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(translatable("commands.fly.disabled" + (self ? ".self" : ".other")));
        } else {
            player.setVelocity(new Vector(0, 0.0125, 0).normalize());
            player.sendMessage(translatable("commands.fly.enabled" + (self ? ".self" : ".other")));
            Bukkit.getScheduler().runTaskLater(
                    Module.module(CommonCommandsSpigotModule.class).plugin(),
                    () -> {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    },
                    2
            );
        }
    }

}