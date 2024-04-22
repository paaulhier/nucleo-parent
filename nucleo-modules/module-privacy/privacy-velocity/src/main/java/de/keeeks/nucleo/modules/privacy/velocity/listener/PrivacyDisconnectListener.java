package de.keeeks.nucleo.modules.privacy.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;

public class PrivacyDisconnectListener {
    private final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);

    @Subscribe
    public void handleDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        privacyApi.invalidatePrivacyInformation(player.getUniqueId());
    }
}