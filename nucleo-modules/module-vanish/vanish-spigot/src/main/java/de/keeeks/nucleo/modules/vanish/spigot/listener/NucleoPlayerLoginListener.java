package de.keeeks.nucleo.modules.vanish.spigot.listener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.spigot.VanishSpigotModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class NucleoPlayerLoginListener implements Listener {
    private final VanishSpigotModule vanishSpigotModule = Module.module(VanishSpigotModule.class);
    private final VanishApi vanishApi = ServiceRegistry.service(VanishApi.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleLogin(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(
                vanishSpigotModule.plugin(),
                () -> {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        vanishSpigotModule.handleVanishing(
                                onlinePlayer,
                                vanishApi.vanishData(onlinePlayer.getUniqueId())
                        );
                    }
                }
        );
    }
}