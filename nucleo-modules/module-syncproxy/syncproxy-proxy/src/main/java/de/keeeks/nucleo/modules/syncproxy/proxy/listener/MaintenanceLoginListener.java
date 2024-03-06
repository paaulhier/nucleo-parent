package de.keeeks.nucleo.modules.syncproxy.proxy.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.syncproxy.proxy.configuration.SyncProxyKickScreenConfiguration;
import de.keeeks.nucleo.modules.syncproxy.proxy.player.FakeSyncProxyPlayer;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class MaintenanceLoginListener implements Listener {
    private final PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
    private final SyncProxyService syncProxyService = ServiceRegistry.service(
            SyncProxyService.class
    );
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Component disconnectScreen;

    public MaintenanceLoginListener(SyncProxyKickScreenConfiguration syncProxyKickScreenConfiguration) {
        this.disconnectScreen =Component.join(
                JoinConfiguration.newlines(),
                syncProxyKickScreenConfiguration.disconnectScreen().stream().map(
                        miniMessage::deserialize
                ).toList()
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleLogin(LoginEvent event) {
        PendingConnection connection = event.getConnection();

        FakeSyncProxyPlayer proxyPlayer = new FakeSyncProxyPlayer(connection);
        PermissionCheckEvent permissionCheckEvent = new PermissionCheckEvent(
                proxyPlayer,
                "nucleo.syncproxy.maintenance.bypass",
                false
        );
        pluginManager.callEvent(permissionCheckEvent);

        if (permissionCheckEvent.hasPermission()) {
            return;
        }

        syncProxyService.currentActiveConfiguration().ifPresent(syncProxyConfiguration -> {
            if (syncProxyConfiguration.maintenance()) {
                event.setCancelled(true);
                event.setReason(BungeeComponentSerializer.get().serialize(
                        disconnectScreen
                )[0]);
            }
        });
    }
}