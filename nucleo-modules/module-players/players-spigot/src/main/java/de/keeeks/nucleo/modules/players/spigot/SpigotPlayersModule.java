package de.keeeks.nucleo.modules.players.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;
import de.keeeks.nucleo.modules.players.shared.updater.PlayerLocaleUpdater;
import de.keeeks.nucleo.modules.players.spigot.listener.NucleoPlayerLoginLocaleApplier;
import de.keeeks.nucleo.modules.players.spigot.packet.listener.NucleoSpigotOnlinePlayerUpdatePacketListener;
import org.bukkit.Bukkit;

@ModuleDescription(
        name = "players",
        description = "The PlayersSpigotModule is responsible for handling player data and events.",
        depends = {"messaging"}
)
public class SpigotPlayersModule extends SpigotModule {
    private static final String nmsVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    @Override
    public void load() {
        ServiceRegistry.registerService(
                PlayerService.class,
                DefaultPlayerService.create()
        );

        try {
            Class<?> playerClass = Class.forName("org.bukkit.craftbukkit.%s.entity.CraftPlayer".formatted(
                    nmsVersion
            ));

            ServiceRegistry.registerService(
                    PlayerLocaleUpdater.class,
                    new PlayerLocaleUpdater(
                            playerClass,
                            this,
                            "adventure$locale"
                    )
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void enable() {
        ServiceRegistry.service(NatsConnection.class).registerPacketListener(
                new NucleoSpigotOnlinePlayerUpdatePacketListener(this)
        );
        registerListener(new NucleoPlayerLoginLocaleApplier());
    }
}