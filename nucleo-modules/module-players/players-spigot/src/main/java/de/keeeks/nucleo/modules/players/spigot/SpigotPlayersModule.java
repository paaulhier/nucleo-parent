package de.keeeks.nucleo.modules.players.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;
import de.keeeks.nucleo.modules.players.spigot.packet.listener.SpigotPlayerUpdatePacketListener;

@ModuleDescription(
        name = "players",
        description = "The PlayersSpigotModule is responsible for handling player data and events.",
        depends = {"messaging"}
)
public class SpigotPlayersModule extends SpigotModule {

    @Override
    public void load() {
        ServiceRegistry.registerService(
                PlayerService.class,
                DefaultPlayerService.create()
        );
    }

    @Override
    public void enable() {
        ServiceRegistry.service(NatsConnection.class).registerPacketListener(
                new SpigotPlayerUpdatePacketListener()
        );
    }
}