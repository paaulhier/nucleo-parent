package de.keeeks.nucleo.modules.vanish.spigot.listener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.vanish.spigot.VanishSpigotModule;
import de.keeeks.nucleo.modules.vanish.spigot.event.PlayerToggleVanishEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NucleoPlayerToggleVanishEventListener implements Listener {
    private final VanishSpigotModule module = Module.module(VanishSpigotModule.class);

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleVanish(PlayerToggleVanishEvent event) {
        Player player = event.getPlayer();
        module.handleVanishing(player, event.vanishData());
    }
}