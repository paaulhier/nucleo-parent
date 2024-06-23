package de.keeeks.nucleo.core.spigot.command;

import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpirationListener;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpiringMap;
import de.keeeks.nucleo.core.spigot.NucleoSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.translatable;

@Command({"stop"})
@CommandPermission("nucleo.command.stop")
public class NucleoStopCommand {
    private static final UUID consoleUUID = new UUID(0, 0);
    private static final Plugin plugin = NucleoSpigotPlugin.plugin();

    private final ExpiringMap<UUID, Long> stopRequests = ExpiringMap.builder()
            .expirationListener((ExpirationListener<UUID, Long>) (uuid, value) -> {
                //Should never happen
                if (uuid.equals(consoleUUID)) return;

                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    player.sendMessage(translatable("nucleo.command.stop.request.expired"));
                }
            })
            .expiration(30, TimeUnit.SECONDS)
            .build();

    @DefaultFor("stop")
    public void stopCommand(BukkitCommandActor bukkitCommandActor) {
        if (bukkitCommandActor.isPlayer()) {
            Player player = bukkitCommandActor.requirePlayer();
            requestStop(player.getUniqueId(), player);
        } else {
            requestStop(null, bukkitCommandActor.getSender());
        }
    }

    private void requestStop(UUID uuid, CommandSender commandSender) {
        if (uuid == null) {
            stopRequests.put(consoleUUID, System.currentTimeMillis());
            confirmStop(consoleUUID, commandSender);
            return;
        }

        if (stopRequests.containsKey(uuid)) {
            commandSender.sendMessage(translatable("nucleo.command.stop.request.alreadyRequested"));
            return;
        }

        stopRequests.put(uuid, System.currentTimeMillis());
        commandSender.sendMessage(translatable("nucleo.command.stop.requested"));
    }

    @Subcommand("confirm")
    public void confirmCommand(BukkitCommandActor bukkitCommandActor) {
        if (bukkitCommandActor.isPlayer()) {
            Player player = bukkitCommandActor.requirePlayer();
            confirmStop(player.getUniqueId(), player);
        }
    }

    private void confirmStop(UUID uuid, CommandSender commandSender) {
        if (!stopRequests.containsKey(uuid)) {
            commandSender.sendMessage(translatable("nucleo.command.stop.request.notRequested"));
            return;
        }

        Scheduler.runAsync(() -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getUniqueId().equals(uuid)) continue;
                kickPlayer(onlinePlayer);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (commandSender instanceof Player player) {
                kickPlayer(player);
            }
            Bukkit.shutdown();
        });
    }

    private void kickPlayer(Player onlinePlayer) {
        Bukkit.getScheduler().runTask(
                plugin,
                () -> onlinePlayer.kick(translatable("nucleo.command.stop.kick"))
        );
    }
}