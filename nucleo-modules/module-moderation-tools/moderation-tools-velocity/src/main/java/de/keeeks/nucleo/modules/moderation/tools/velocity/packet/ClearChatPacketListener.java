package de.keeeks.nucleo.modules.moderation.tools.velocity.packet;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearChatPacket;
import net.kyori.adventure.text.Component;

import java.util.UUID;

import static net.kyori.adventure.text.Component.translatable;

public abstract class ClearChatPacketListener<P extends ClearChatPacket> extends PacketListener<P> {
    public ClearChatPacketListener(Class<P> packetClass) {
        super(packetClass);
    }

    protected final void clearChat(Player player, UUID executor) {
        for (int i = 0; i < 1000; i++) player.sendMessage(Component.empty());
        player.sendMessage(translatable(
                "nucleo.moderation.chatClear",
                NameColorizer.coloredName(executor)
        ));
    }
}