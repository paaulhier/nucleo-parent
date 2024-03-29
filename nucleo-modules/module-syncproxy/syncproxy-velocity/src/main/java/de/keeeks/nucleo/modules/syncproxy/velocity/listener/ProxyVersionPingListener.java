package de.keeeks.nucleo.modules.syncproxy.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.keeeks.nucleo.modules.players.api.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ProxyVersionPingListener {
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

    @Subscribe(order = PostOrder.EARLY)
    public EventTask handlePing(ProxyPingEvent event) {
        int protocol = event.getConnection().getProtocolVersion().getProtocol();

        return EventTask.async(() -> {
            ServerPing.Builder builder = event.getPing().asBuilder();
            Version version = Version.byProtocol(protocol);

            if (version == null) {
                builder.version(new ServerPing.Version(
                        -1,
                        Version.supportedVersionsAsString()
                ));
                builder.description(unsupportedMotd);
                event.setPing(builder.build());
            }
        });
    }
}