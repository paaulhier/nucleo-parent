package de.keeeks.nucleo.modules.players.velocity;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;
import de.keeeks.nucleo.modules.players.velocity.listener.LoginListener;
import de.keeeks.nucleo.modules.players.velocity.listener.PlayerDisconnectListener;
import de.keeeks.nucleo.modules.players.velocity.listener.ServerConnectedListener;
import de.keeeks.nucleo.modules.players.velocity.packet.listener.NucleoOnlinePlayerConnectRequestPacketListener;
import de.keeeks.nucleo.modules.players.velocity.packet.listener.NucleoOnlinePlayerMessagePacketListener;

@ModuleDescription(
        name = "players",
        description = "The PlayersProxyModule is responsible for handling player data and events.",
        depends = {"messaging"}
)
public class PlayersVelocityModule extends VelocityModule {
    @Override
    public void load() {
        PlayerService playerService = ServiceRegistry.registerService(
                PlayerService.class,
                DefaultPlayerService.create()
        );

        autoCompleter().registerSuggestion(
                "players",
                (list, commandActor, executableCommand) -> playerService.onlinePlayers().stream()
                        .map(NucleoPlayer::name)
                        .toList()
        );
    }

    @Override
    public void enable() {
        registerListener(
                new LoginListener(),
                new ServerConnectedListener(),
                new PlayerDisconnectListener()
        );

        ServiceRegistry.service(NatsConnection.class).registerPacketListener(
                new NucleoOnlinePlayerMessagePacketListener(plugin.proxyServer()),
                new NucleoOnlinePlayerConnectRequestPacketListener(plugin.proxyServer())
        );
    }
}