package de.keeeks.nucleo.modules.players.velocity;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.ClientBrand;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;
import de.keeeks.nucleo.modules.players.velocity.listener.*;
import de.keeeks.nucleo.modules.players.velocity.packet.listener.NucleoOnlinePlayerConnectRequestPacketListener;
import de.keeeks.nucleo.modules.players.velocity.packet.listener.VelocityNucleoOnlinePlayerExecuteCommandPacketListener;
import de.keeeks.nucleo.modules.players.velocity.packet.listener.NucleoOnlinePlayerKickPacketListener;
import de.keeeks.nucleo.modules.players.velocity.packet.listener.NucleoOnlinePlayerMessagePacketListener;

import java.util.UUID;
import java.util.regex.Pattern;

@ModuleDescription(
        name = "players",
        description = "The PlayersProxyModule is responsible for handling player data and events.",
        dependencies = @Dependency(name = "messaging")
)
public class PlayersVelocityModule extends VelocityModule {
    private static final Pattern uuidPattern = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private PlayerService playerService;

    @Override
    public void load() {
        playerService = ServiceRegistry.registerService(
                PlayerService.class,
                DefaultPlayerService.create()
        );

        autoCompleter().registerSuggestion(
                "players",
                (list, commandActor, executableCommand) -> playerService.players().stream()
                        .map(NucleoPlayer::name)
                        .toList()
        );
        autoCompleter().registerSuggestion(
                "onlinePlayers",
                (list, commandActor, executableCommand) -> playerService.onlinePlayers().stream()
                        .map(NucleoPlayer::name)
                        .toList()
        );
    }

    @Override
    public void enable() {
        registerListener(
                new LoginListener(),
                new PreLoginListener(),
                new ClientBrandListener(),
                new ServerConnectedListener(),
                new PlayerDisconnectListener()
        );

        commandHandler().registerValueResolver(
                NucleoOnlinePlayer.class,
                valueResolverContext -> {
                    String argument = valueResolverContext.pop();
                    if (uuidPattern.matcher(argument).matches()) {
                        return playerService.onlinePlayer(UUID.fromString(argument)).orElse(null);
                    }
                    return playerService.onlinePlayer(argument).orElse(null);
                }
        );
        commandHandler().registerValueResolver(
                NucleoPlayer.class,
                valueResolverContext -> {
                    String argument = valueResolverContext.pop();
                    if (uuidPattern.matcher(argument).matches()) {
                        return playerService.player(UUID.fromString(argument)).orElse(null);
                    }
                    return playerService.player(argument).orElse(null);
                }
        );

        ServiceRegistry.service(NatsConnection.class).registerPacketListener(
                new NucleoOnlinePlayerMessagePacketListener(plugin.proxyServer()),
                new NucleoOnlinePlayerConnectRequestPacketListener(plugin.proxyServer()),
                new NucleoOnlinePlayerKickPacketListener(plugin.proxyServer()),
                new VelocityNucleoOnlinePlayerExecuteCommandPacketListener(plugin.proxyServer())
        );
    }
}