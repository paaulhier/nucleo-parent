package de.keeeks.nucleo.modules.players.api;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface NucleoMessageSender {

    /**
     * Returns the unique identifier of the player who is the owner of this message sender.
     *
     * @return the unique identifier of the player who is the owner of this message sender
     */
    UUID uuid();

    /**
     * Sends a message to the player. The message type is {@link MessageType#CHAT}. The message will be displayed in the chat.
     *
     * @param component   the message to send
     * @param messageType the message type
     */
    void send(Component component, MessageType messageType);

    /**
     * Sends a message to the player. The message will be displayed in the chat.
     *
     * @param component the message to send
     */
    default void sendMessage(Component component) {
        send(component, MessageType.CHAT);
    }

    /**
     * Sends an action bar message to the player. The message will be displayed above the hotbar.
     *
     * @param component the message to send
     */
    default void sendActionBar(Component component) {
        send(component, MessageType.ACTIONBAR);
    }

    enum MessageType {
        CHAT,
        ACTIONBAR
    }
}