package de.keeeks.nucleo.modules.players.spigot.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.shared.updater.PlayerLocaleUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class NucleoPlayerLoginLocaleApplier implements Listener {
    private final PlayerLocaleUpdater playerLocaleUpdater = ServiceRegistry.service(
            PlayerLocaleUpdater.class
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleLogin(PlayerLoginEvent event) {
        playerLocaleUpdater.updateLocale(
                event.getPlayer(),
                event.getPlayer().locale()
        );
    }
}