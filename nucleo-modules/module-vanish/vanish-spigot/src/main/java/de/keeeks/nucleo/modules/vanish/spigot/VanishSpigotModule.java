package de.keeeks.nucleo.modules.vanish.spigot;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.api.VanishData;
import de.keeeks.nucleo.modules.vanish.shared.NucleoVanishApi;
import de.keeeks.nucleo.modules.vanish.spigot.event.PlayerVanishForPlayerEvent;
import de.keeeks.nucleo.modules.vanish.spigot.listener.NucleoPlayerLoginListener;
import de.keeeks.nucleo.modules.vanish.spigot.listener.NucleoPlayerToggleVanishEventListener;
import de.keeeks.nucleo.modules.vanish.spigot.packetlistener.SpigotVanishDataUpdatePacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@ModuleDescription(
        name = "vanish",
        dependencies = {
                @Dependency(name = "messaging"),
                @Dependency(name = "players")
        }
)
public class VanishSpigotModule extends SpigotModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                VanishApi.class,
                new NucleoVanishApi()
        );
    }

    @Override
    public void enable() {
        ServiceRegistry.service(NatsConnection.class).registerPacketListener(
                new SpigotVanishDataUpdatePacketListener()
        );
        registerListener(
                new NucleoPlayerToggleVanishEventListener(),
                new NucleoPlayerLoginListener()
        );
    }

    public void handleVanishing(Player player, VanishData vanishData) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            PlayerVanishForPlayerEvent event = new PlayerVanishForPlayerEvent(player, onlinePlayer, vanishData);
            Bukkit.getPluginManager().callEvent(event);

            if (event.cancelled()) continue;

           Bukkit.getScheduler().runTask(plugin(), () -> {
               if (vanishData.vanished()) {
                   onlinePlayer.hidePlayer(plugin(), player);
               } else {
                   onlinePlayer.showPlayer(plugin(), player);
               }
           });
        }
    }
}