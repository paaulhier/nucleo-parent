package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.administration;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpirationListener;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpiringMap;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket.State;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.CommandHandlerVisitor;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Command("send")
@CommandPermission("nucleo.command.send")
public class SendCommand implements CommandHandlerVisitor {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final ExpiringMap<UUID, RegisteredServer> sendAllConfirmations;

    private final ProxyServer proxyServer;

    {
        sendAllConfirmations = ExpiringMap.builder()
                .expiration(30, TimeUnit.SECONDS)
                .expirationListener(new ExpirationListener<UUID, RegisteredServer>() {
                    @Override
                    public void expired(UUID key, RegisteredServer value) {
                        proxyServer.getPlayer(key).ifPresent(
                                player -> player.sendMessage(translatable("nucleo.command.send.sendAllExpired"))
                        );
                    }
                })
                .build();
    }

    public SendCommand(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Usage("nucleo.command.send.usage")
    @AutoComplete("@players @servers")
    @DefaultFor("send")
    public void sendCommand(
            Player player,
            String playerName,
            RegisteredServer serverConnectTo
    ) {
        if (serverConnectTo == null) {
            player.sendMessage(translatable("nucleo.command.send.serverNotFound"));
            return;
        }

        proxyServer.getServer(playerName).ifPresentOrElse(requestedServer -> {
            if (requestedServer.equals(serverConnectTo)) {
                player.sendMessage(translatable("nucleo.command.send.sendSameServer"));
                return;
            }
            connectAllPlayersOfServer(player, playerName, serverConnectTo);
        }, () -> playerService.onlinePlayer(playerName).ifPresentOrElse(
                onlinePlayer -> connectPlayer(player, serverConnectTo, onlinePlayer),
                () -> player.sendMessage(translatable("playerNotOnline"))
        ));
    }

    private void connectPlayer(Player player, RegisteredServer serverConnectTo, NucleoOnlinePlayer onlinePlayer) {
        onlinePlayer.connect(
                serverConnectTo.getServerInfo().getName(), state -> {
                    if (state.successful()) {
                        player.sendMessage(translatable(
                                "nucleo.command.send.sendSuccess",
                                NameColorizer.coloredName(onlinePlayer.uuid()),
                                text(serverConnectTo.getServerInfo().getName())
                        ));
                        return;
                    }

                    player.sendMessage(translatable(
                            "nucleo.command.send.sendFailed",
                            NameColorizer.coloredName(onlinePlayer.uuid()),
                            text(serverConnectTo.getServerInfo().getName()),
                            text(state.name())
                    ));
                }
        );
    }

    private void connectAllPlayersOfServer(Player player, String serverName, RegisteredServer serverConnectTo) {
        List<NucleoOnlinePlayer> players = playerService.onlinePlayers(serverName);
        sendPlayersToServer(player, serverConnectTo, players, stateIntegerMap -> player.sendMessage(translatable(
                "nucleo.command.send.sendAllOfServerSuccess",
                text(stateIntegerMap.get(State.SUCCESS)),
                text(stateIntegerMap.get(State.ALREADY_CONNECTED)),
                text(stateIntegerMap.get(State.CONNECTION_CANCELLED)),
                text(stateIntegerMap.get(State.CONNECTION_IN_PROGRESS)),
                text(stateIntegerMap.get(State.SERVER_DISCONNECTED)),
                text(stateIntegerMap.get(State.SERVER_NOT_FOUND))
        )));
    }

    private static void sendPlayersToServer(
            Player player,
            RegisteredServer serverConnectTo,
            List<NucleoOnlinePlayer> players,
            Consumer<Map<State, Integer>> callback
    ) {
        Map<State, Integer> states = new HashMap<>() {{
            put(State.SUCCESS, 0);
            put(State.ALREADY_CONNECTED, 0);
            put(State.CONNECTION_CANCELLED, 0);
            put(State.CONNECTION_IN_PROGRESS, 0);
            put(State.SERVER_DISCONNECTED, 0);
            put(State.SERVER_NOT_FOUND, 0);
        }};
        int playerCount = players.size();
        for (NucleoOnlinePlayer onlinePlayer : players) {
            onlinePlayer.connect(
                    serverConnectTo.getServerInfo().getName(),
                    state -> states.compute(
                            state,
                            (state1, integer) -> integer == null ? 1 : integer + 1
                    )
            );
        }

        Scheduler.runAsync(() -> {
            long startedAt = System.currentTimeMillis();
            while (playerCount > states.values().stream().mapToInt(Integer::intValue).sum()) {
                if (System.currentTimeMillis() - startedAt > 5000) {
                    player.sendMessage(translatable(
                            "nucleo.command.send.sendTimeout",
                            text(playerCount),
                            text(states.values().stream().mapToInt(Integer::intValue).sum())
                    ));
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            callback.accept(states);
        });
    }

    @Override
    public void visit(@NotNull CommandHandler commandHandler) {
        commandHandler.register(new SendAllCommand());
    }

    @Command("send all")
    public class SendAllCommand {

        @AutoComplete("@servers")
        @DefaultFor("~")
        public void sendAllCommand(Player player, RegisteredServer serverConnectTo) {
            if (sendAllConfirmations.containsKey(player.getUniqueId())) {
                player.sendMessage(translatable("nucleo.command.send.sendAllAlreadyRequested"));
                return;
            }
            sendAllConfirmations.put(
                    player.getUniqueId(),
                    serverConnectTo
            );
            player.sendMessage(translatable("nucleo.command.send.sendAllRequested"));
        }

        @Subcommand("confirm")
        public void sendAllConfirmCommand(Player player) {
            if (!sendAllConfirmations.containsKey(player.getUniqueId())) {
                player.sendMessage(translatable("nucleo.command.send.sendAllNoRequest"));
                return;
            }
            RegisteredServer registeredServer = sendAllConfirmations.remove(player.getUniqueId());

            if (registeredServer == null) {
                player.sendMessage(translatable("nucleo.command.send.sendAllNoRequest"));
                return;
            }

            List<NucleoOnlinePlayer> onlinePlayers = playerService.onlinePlayers();

            sendPlayersToServer(
                    player,
                    registeredServer,
                    onlinePlayers,
                    states -> player.sendMessage(translatable(
                            "nucleo.command.send.sendAllSuccess",
                            text(states.get(State.SUCCESS)),
                            text(states.get(State.ALREADY_CONNECTED)),
                            text(states.get(State.CONNECTION_CANCELLED)),
                            text(states.get(State.CONNECTION_IN_PROGRESS)),
                            text(states.get(State.SERVER_DISCONNECTED)),
                            text(states.get(State.SERVER_NOT_FOUND))
                    ))
            );
        }
    }
}