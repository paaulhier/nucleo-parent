package de.keeeks.nucleo.modules.common.commands.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.translation.GlobalTranslator;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.kyori.adventure.text.Component.*;

@Command({"list", "glist", "online", "players"})
@RequiredArgsConstructor
public class ListCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    private final ProxyServer proxyServer;

    @AutoComplete("@servers")
    @DefaultFor("~")
    public void listCommand(
            Player player,
            @Optional String server
    ) {
        Scheduler.runAsync(() -> {
            if (server == null || !player.hasPermission("nucleo.command.list.server")) {
                int playerCount = playerService.onlinePlayerCount();
                player.sendMessage(translatable(
                        "nucleo.command.list.players.%s".formatted(
                                playerCount == 1 ? "singular" : "plural"
                        ),
                        text(playerCount)
                ));
                return;
            }

            proxyServer.getServer(server).ifPresentOrElse(registeredServer -> player.sendMessage(translatable(
                    "nucleo.command.list.players.server",
                    text(registeredServer.getServerInfo().getName()),
                    text(playerService.onlinePlayers(server).size())
            )), () -> player.sendMessage(translatable(
                    "nucleo.command.list.players.serverNotFound",
                    text(server)
            )));
        });
    }

    @Subcommand("detailed")
    @CommandPermission("nucleo.command.list.detailed")
    public void detailedCommand(Player player) {
        Scheduler.runAsync(() -> {
            Map<String, Integer> serverOnlineCountMap = new HashMap<>();

            for (NucleoOnlinePlayer onlinePlayer : playerService.onlinePlayers()) {
                serverOnlineCountMap.compute(
                        onlinePlayer.server(),
                        (serverName, currentCount) -> currentCount == null ? 1 : currentCount + 1
                );
            }

            player.sendMessage(translatable(
                    "nucleo.command.list.players.detailed",
                    join(
                            JoinConfiguration.newlines(),
                            serverOnlineCountMap.keySet().stream().map(s -> GlobalTranslator.render(
                                    translatable(
                                            "nucleo.command.list.players.detailed.server",
                                            text(s),
                                            text(serverOnlineCountMap.get(s))
                                    ),
                                    Objects.requireNonNull(player.getEffectiveLocale(), "Locale can not be null!")
                            )).toList()
                    )
            ));
        });
    }
}