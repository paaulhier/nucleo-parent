package de.keeeks.nucleo.modules.players.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
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
        ServiceRegistry.registerService(
                PlayerService.class,
                DefaultPlayerService.create()
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