package de.keeeks.nucleo.modules.teamchat.listener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.teamchat.packet.TeamChatMessagePacket;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@RequiredArgsConstructor
public class TeamChatChatListener implements Listener {
    private final NatsConnection natsConnection;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleChat(ChatEvent event) {
        if (event.isCancelled()) return;
        if (event.isCommand() || event.isProxyCommand()) return;

        if (event.getSender() instanceof ProxiedPlayer sender) {
            String message = event.getMessage();
            if (message.startsWith("!")) {
                event.setCancelled(true);
                natsConnection.publishPacket(
                        "nucleo:teamchat",
                        new TeamChatMessagePacket(
                                message.substring(1),
                                sender.getUniqueId(),
                                sender.getServer().getInfo().getName(),
                                Module.serviceName()
                        )
                );
            }
        }
    }
}