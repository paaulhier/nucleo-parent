package de.keeeks.nucleo.core.spigot.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NucleoPluginMessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(
            @NotNull String channel,
            @NotNull Player player,
            byte[] bytes
    ) {
        ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(bytes);

        String subChannel = byteArrayDataInput.readUTF();

        if (subChannel.equals("message")) {
            UUID uuid = new UUID(
                    byteArrayDataInput.readLong(),
                    byteArrayDataInput.readLong()
            );
            String message = byteArrayDataInput.readUTF();

            Player target = Bukkit.getPlayer(uuid);
            if (target == null) return;

            target.performCommand(message);
        }
    }
}