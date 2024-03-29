package de.keeeks.nucleo.modules.syncproxy.velocity.configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public record SyncProxyKickScreenConfiguration(
        List<String> disconnectScreen
) {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public Component component() {
        return miniMessage.deserialize(String.join("\n", disconnectScreen));
    }

    public static SyncProxyKickScreenConfiguration defaultConfiguration() {
        return new SyncProxyKickScreenConfiguration(List.of(
                "<red><bold>Server is currently in maintenance mode.",
                "<red><bold>Please try again later."
        ));
    }
}