package de.keeeks.nucleo.modules.players.server;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.server.packetlistener.PlayersPrivacyInformationDeclinedPacketListener;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;

@ModuleDescription(
        name = "players",
        description = "The PlayerServerModule is responsible for handling player data (like deleting) and events.",
        depends = {"messaging", "database-mysql", "privacy"}
)
public class PlayerServerModule extends Module {

    @Override
    public void load() {
        ServiceRegistry.registerService(
                PlayerService.class,
                DefaultPlayerService.create()
        );
    }

    @Override
    public void enable() {
        NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);
        natsConnection.registerPacketListener(
                new PlayersPrivacyInformationDeclinedPacketListener()
        );
    }
}