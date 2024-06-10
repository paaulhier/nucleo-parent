package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.administration;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastApi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.translatable;

@Command({"broadcast", "bc"})
@Usage("nucleo.moderation.tools.broadcast.usage")
@CommandPermission("nucleo.moderation.tools.broadcast")
public final class BroadcastCommand {
    private final BroadcastApi broadcastApi = ServiceRegistry.service(BroadcastApi.class);
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @DefaultFor("~")
    public void broadcast(
            Player player,
            @Flag("permission") @Optional String permission,
            @Flag("server") @Optional String server,
            String message
    ) {
        Component component = miniMessage.deserialize(message);
        broadcastApi.broadcast(component, broadcastOptions -> {
            if (permission != null) {
                broadcastOptions.permission(permission);
            }
            if (server != null) {
                broadcastOptions.server(server);
            }
            return broadcastOptions;
        });
        player.sendMessage(translatable("nucleo.moderation.tools.broadcast.broadcasted"));
    }
}