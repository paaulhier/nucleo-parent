package de.keeeks.nucleo.modules.moderation.tools.chatclear;

import java.util.UUID;

public interface ChatClearApi {
    String CHANNEL = "nucleo:chatclear";

    void clearGlobalChat(UUID executor);

    void clearChat(UUID player, UUID executor);

    void clearChat(String server, UUID executor);
}