package de.keeeks.nucleo.modules.players.api;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface NucleoMessageSender {

    UUID uuid();

    void send(Component component, MessageType messageType);

    default void sendMessage(Component component) {
        send(component, MessageType.CHAT);
    }

    default void sendActionBar(Component component) {
        send(component, MessageType.ACTIONBAR);
    }

    enum MessageType {
        CHAT,
        ACTIONBAR
    }
}