package de.keeeks.nucleo.modules.moderation.tools.shared.chatclear;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.ChatClearApi;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearGlobalChatPacket;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearPlayerChatPacket;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.packet.ClearServerChatPacket;

import java.util.UUID;

public class NucleoChatClearApi implements ChatClearApi {
    private final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    @Override
    public void clearGlobalChat(UUID executor) {
        natsConnection.publishPacket(
                CHANNEL,
                new ClearGlobalChatPacket(executor)
        );
    }

    @Override
    public void clearChat(UUID player, UUID executor) {
        natsConnection.publishPacket(
                CHANNEL,
                new ClearPlayerChatPacket(player, executor)
        );
    }

    @Override
    public void clearChat(String server, UUID executor) {
        natsConnection.publishPacket(
                CHANNEL,
                new ClearServerChatPacket(server, executor)
        );
    }
}