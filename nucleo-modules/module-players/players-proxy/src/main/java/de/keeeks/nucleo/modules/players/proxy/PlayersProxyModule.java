package de.keeeks.nucleo.modules.players.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.proxy.listener.LoginListener;
import de.keeeks.nucleo.modules.players.proxy.listener.PlayerDisconnectListener;
import de.keeeks.nucleo.modules.players.proxy.listener.ServerConnectedListener;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;

@ModuleDescription(
        name = "players",
        description = "The PlayersProxyModule is responsible for handling player data and events.",
        depends = {"messaging"}
)
public class PlayersProxyModule extends ProxyModule {
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
    }
}