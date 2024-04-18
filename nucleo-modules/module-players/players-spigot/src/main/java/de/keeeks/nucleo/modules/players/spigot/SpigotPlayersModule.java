package de.keeeks.nucleo.modules.players.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.DefaultPlayerService;

@ModuleDescription(
        name = "players",
        description = "The PlayersSpigotModule is responsible for handling player data and events.",
        depends = {"messaging"}
)
public class SpigotPlayersModule extends SpigotModule {
    private PlayerService playerService;

    @Override
    public void load() {
        playerService = ServiceRegistry.registerService(
                PlayerService.class,
                DefaultPlayerService.create()
        );
    }

    @Override
    public void enable() {
        commandHandler().registerValueResolver(
                NucleoOnlinePlayer.class,
                valueResolverContext -> {
                    String playerName = valueResolverContext.pop();
                    return playerService.onlinePlayer(playerName).orElse(null);
                }
        );
        commandHandler().registerValueResolver(
                NucleoPlayer.class,
                valueResolverContext -> {
                    String playerName = valueResolverContext.pop();
                    return playerService.player(playerName).orElse(null);
                }
        );
    }
}