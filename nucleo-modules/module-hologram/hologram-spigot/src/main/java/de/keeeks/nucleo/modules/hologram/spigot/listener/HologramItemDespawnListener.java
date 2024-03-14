package de.keeeks.nucleo.modules.hologram.spigot.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.hologram.api.Hologram;
import de.keeeks.nucleo.modules.hologram.api.HologramApi;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

import java.util.UUID;

public class HologramItemDespawnListener implements Listener {
    private final HologramApi hologramApi = ServiceRegistry.service(
            HologramApi.class
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleDespawn(ItemDespawnEvent event) {
        Item entity = event.getEntity();

        if (!entity.hasMetadata("hologram")) return;

        entity.getMetadata("hologram").forEach(metadata -> {
            Hologram hologram = hologramApi.hologram(UUID.fromString(metadata.asString()));
            if (hologram == null) return;
            event.setCancelled(true);
        });
    }
}