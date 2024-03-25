package de.keeeks.nucleo.modules.common.commands.spigot.commands;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.common.commands.spigot.CommonCommandsSpigotModule;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("fly")
@CommandPermission("nucleo.commands.fly")
public class FlyCommand {

    @DefaultFor("fly")
    public void flyCommand(
            Player player,
            @Optional String targetName
    ) {
        if (targetName == null || targetName.equalsIgnoreCase(player.getName())) {
            toggleFlyForSelf(player);
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(Component.translatable("playerNotFound"));
            return;
        }

        toggleFlyForOther(player, target);
    }

    private void toggleFlyForOther(Player player, Player target) {
        if (toggleFlyFor(target)) {
            player.sendMessage(Component.translatable(
                    "commands.fly.enabled.other",
                    Component.text(target.getName())
            ));
        } else {
            player.sendMessage(Component.translatable(
                    "commands.fly.disabled.other",
                    Component.text(target.getName())
            ));
        }
    }

    private void toggleFlyForSelf(Player player) {
        if (toggleFlyFor(player)) {
            player.sendMessage(Component.translatable("commands.fly.enabled.self"));
        } else {
            player.sendMessage(Component.translatable("commands.fly.disabled.self"));
        }
    }

    private boolean toggleFlyFor(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            return false;
        } else {
            player.setVelocity(new Vector(0, 0.0125, 0).normalize());
            Bukkit.getScheduler().runTaskLater(
                    Module.module(CommonCommandsSpigotModule.class).plugin(),
                    () -> {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    },
                    2
            );
            return true;
        }
    }

}