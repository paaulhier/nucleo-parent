package de.keeeks.nucleo.modules.syncproxy.velocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;

import static net.kyori.adventure.text.Component.translatable;

public class MaintenanceLoginListener {
    private final SyncProxyService syncProxyService = ServiceRegistry.service(
            SyncProxyService.class
    );

    @Subscribe(order = PostOrder.FIRST)
    public void handleLogin(LoginEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("nucleo.syncproxy.maintenance.bypass")) {
            return;
        }

        syncProxyService.currentActiveConfiguration().ifPresent(syncProxyConfiguration -> {
            if (syncProxyConfiguration.maintenance()) {
                event.setResult(ResultedEvent.ComponentResult.denied(translatable(
                        "nucleo.syncproxy.maintenance.kick"
                )));
            }
        });
    }
}