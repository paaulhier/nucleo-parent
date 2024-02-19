package de.keeeks.nucleo.modules.syncproxy.proxy.listener;

import de.keeeks.nucleo.core.proxy.NucleoProxyPlugin;
import de.keeeks.nucleo.modules.players.api.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyVersionPingListener implements Listener {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Component firstUnsupportedMotdLine = miniMessage.deserialize(
            "<gold><bold>Keeeks <reset><dark_gray>» <gray>Dein Minecraft Netzwerk  <dark_gray>[<red>1.18.x <dark_gray>- <red>1.20<dark_gray>]"
    );
    private final Component secondUnsupportedMotdLine = miniMessage.deserialize(
            "<gray>Unser Netzwerk unterstützt <red>1.18.x <gray>bis <red>1.20<gray>."
    );
    private final Component unsupportedMotd = firstUnsupportedMotdLine.append(Component.newline()).append(
            secondUnsupportedMotdLine
    );

    private final BungeeComponentSerializer bungeeComponentSerializer = BungeeComponentSerializer.of(
            GsonComponentSerializer.gson(),
            LegacyComponentSerializer.legacy('&')
    );
    private final NucleoProxyPlugin plugin = NucleoProxyPlugin.plugin();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleProxyPing(ProxyPingEvent event) {
        event.registerIntent(plugin);
        Version version = Version.byProtocol(event.getConnection().getVersion());
        ServerPing response = event.getResponse();

        if (version == null) {
            response.setVersion(new ServerPing.Protocol(
                    Version.supportedVersionsAsString(),
                    -1
            ));
            response.setDescriptionComponent(bungeeComponentSerializer.serialize(unsupportedMotd)[0]);
        }
        event.setResponse(response);
        event.completeIntent(plugin);
    }

}