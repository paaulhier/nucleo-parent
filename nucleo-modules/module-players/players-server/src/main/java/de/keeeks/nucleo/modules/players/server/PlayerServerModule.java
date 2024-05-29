package de.keeeks.nucleo.modules.players.server;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.server.packetlistener.PlayersPrivacyInformationDeclinedPacketListener;
import de.keeeks.nucleo.modules.players.server.requests.PlayerCountRequestHandler;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;
import de.keeeks.nucleo.modules.web.WebModule;
import de.keeeks.nucleo.modules.web.handler.RequestHandlerRegistrar;
import de.keeeks.nucleo.modules.web.handler.socket.SocketHandler;

@ModuleDescription(
        name = "players",
        description = "The PlayerServerModule is responsible for handling player data (like deleting) and events.",
        dependencies = {
                @Dependency(name = "messaging"),
                @Dependency(name = "database-mysql"),
                @Dependency(name = "privacy")
        }
)
public class PlayerServerModule extends WebModule {

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

        RequestHandlerRegistrar.register(new PlayerCountRequestHandler(this));
    }

    @Override
    public void disable() {
        SocketHandler.closeAllSessions();
    }
}