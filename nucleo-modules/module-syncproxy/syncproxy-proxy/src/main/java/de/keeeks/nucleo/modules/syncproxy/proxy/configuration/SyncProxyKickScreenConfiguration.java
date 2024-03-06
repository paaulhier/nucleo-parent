package de.keeeks.nucleo.modules.syncproxy.proxy.configuration;

import java.util.List;

public record SyncProxyKickScreenConfiguration(
        List<String> disconnectScreen
) {
    public static SyncProxyKickScreenConfiguration defaultConfiguration() {
        return new SyncProxyKickScreenConfiguration(List.of(
                "<red><bold>Server is currently in maintenance mode.",
                "<red><bold>Please try again later."
        ));
    }
}