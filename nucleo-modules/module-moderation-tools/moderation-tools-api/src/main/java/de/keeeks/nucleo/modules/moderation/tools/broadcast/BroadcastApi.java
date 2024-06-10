package de.keeeks.nucleo.modules.moderation.tools.broadcast;

import net.kyori.adventure.text.Component;

public interface BroadcastApi {
    String CHANNEL = "nucleo:broadcast";

    void broadcast(Component component);

    void broadcast(Component component, BroadcastOptionsBuilder broadcastOptionsBuilder);
}