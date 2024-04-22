package de.keeeks.nucleo.modules.privacy.spigot.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.spigot.inventory.PrivacyInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PrivacyJoinListener implements Listener {
    private final PrivacyInventory privacyInventory = new PrivacyInventory();

    private final PrivacyApi privacyApi = ServiceRegistry.service(PrivacyApi.class);

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        privacyApi.privacyInformation(player.getUniqueId()).ifPresentOrElse(privacyInformation -> {
            if (privacyInformation.accepted()) {
                return;
            }
            privacyInventory.open(player);
        }, () -> privacyInventory.open(player));
    }
}