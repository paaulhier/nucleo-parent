package de.keeeks.nucleo.modules.privacy.spigot.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PrivacyQuitListener implements Listener {
    private final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        privacyApi.invalidatePrivacyInformation(player.getUniqueId());
    }
}