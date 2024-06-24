package de.keeeks.nucleo.modules.moderation.tools.broadcast;

import net.kyori.adventure.text.Component;

public interface BroadcastApi {
    String CHANNEL = "nucleo:broadcast";

    /**
     * Broadcasts a message to all players on the server.
     *
     * @param component the message to broadcast
     */
    void broadcast(Component component);

    /**
     * Broadcasts a message to all players on the server.
     *
     * @param component               the message to broadcast
     * @param broadcastOptionsBuilder the options for the broadcast see {@link BroadcastOptionsBuilder}
     */
    void broadcast(Component component, BroadcastOptionsBuilder broadcastOptionsBuilder);
}