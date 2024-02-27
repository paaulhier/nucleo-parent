package de.keeeks.nucleo.core.proxy.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public abstract class RedirectableCommand {

    protected void sendMessageOnServer(
            ProxiedPlayer player,
            String message
    ) {
        player.getServer().sendData(
                "nucleo:main",
                createDataOutput(
                        player.getUniqueId(),
                        message
                ).toByteArray()
        );
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