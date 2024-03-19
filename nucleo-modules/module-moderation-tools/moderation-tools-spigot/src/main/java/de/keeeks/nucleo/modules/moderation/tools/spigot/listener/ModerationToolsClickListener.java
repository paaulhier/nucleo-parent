package de.keeeks.nucleo.modules.moderation.tools.spigot.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.moderation.tools.spigot.cps.ClicksPerSecondProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ModerationToolsClickListener implements Listener {
    private final ClicksPerSecondProvider clicksPerSecondProvider = ServiceRegistry.service(
            ClicksPerSecondProvider.class
    );

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        clicksPerSecondProvider.handleClick(event.getPlayer());
    }

    @EventHandler
    public void handleClick(PlayerAnimationEvent event) {
        clicksPerSecondProvider.handleClick(event.getPlayer());
    }
}