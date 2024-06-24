package de.keeeks.nucleo.modules.moderation.tools.chatclear;

import java.util.UUID;

public interface ChatClearApi {
    String CHANNEL = "nucleo:chatclear";

    /**
     * Clears the global chat for all players on the network
     *
     * @param executor the executor of the command
     */
    void clearGlobalChat(UUID executor);

    /**
     * Clears the global chat for all players on the network
     *
     * @param executor the executor of the command
     */
    void clearChat(UUID player, UUID executor);

    /**
     * Clears the chat of a specific server
     *
     * @param server   the server to clear the chat of
     * @param executor the executor of the command
     */
    void clearChat(String server, UUID executor);
}