package de.keeeks.nucleo.modules.common.commands.velocity.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;

import java.util.UUID;

public abstract class RedirectableCommand {

    protected void sendMessageOnServer(
            Player player,
            String message
    ) {
        player.getCurrentServer().ifPresent(serverConnection -> serverConnection.sendPluginMessage(
                () -> "nucleo:main",
                createDataOutput(
                        player.getUniqueId(),
                        message
                ).toByteArray()
        ));
    }

    protected ByteArrayDataOutput createDataOutput(
            UUID uuid,
            String message
    ) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("message");
        byteArrayDataOutput.writeLong(uuid.getMostSignificantBits());
        byteArrayDataOutput.writeLong(uuid.getLeastSignificantBits());
        byteArrayDataOutput.writeUTF(message);
        return byteArrayDataOutput;
    }

}