package de.keeeks.nucleo.modules.syncproxy.proxy.configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.List;

public record SyncProxyKickScreenConfiguration(
        List<String> disconnectScreen
) {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final BungeeComponentSerializer bungeeComponentSerializer = BungeeComponentSerializer.get();

    public List<Component> toComponents() {
        return disconnectScreen.stream().map(
                miniMessage::deserialize
        ).toList();
    }

    public BaseComponent toBungeeComponent() {
        return bungeeComponentSerializer.serialize(Component.join(
                JoinConfiguration.newlines(),
                toComponents()
        ))[0];
    }

    public static SyncProxyKickScreenConfiguration defaultConfiguration() {
        return new SyncProxyKickScreenConfiguration(List.of(
                "<red><bold>Server is currently in maintenance mode.",
                "<red><bold>Please try again later."
        ));
    }
}