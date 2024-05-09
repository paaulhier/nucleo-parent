package de.keeeks.nucleo.modules.players.velocity.packet.listener;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectRequestPacket;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerConnectResponsePacket.State;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoOnlinePlayerConnectRequestPacketListener extends PacketListener<NucleoOnlinePlayerConnectRequestPacket> {
    private final ProxyServer proxyServer;

    public NucleoOnlinePlayerConnectRequestPacketListener(ProxyServer proxyServer) {
        super(NucleoOnlinePlayerConnectRequestPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerConnectRequestPacket nucleoOnlinePlayerConnectRequestPacket,
            Message message
    ) {
        NucleoOnlinePlayer nucleoOnlinePlayer = nucleoOnlinePlayerConnectRequestPacket.player();
        proxyServer.getPlayer(nucleoOnlinePlayer.uuid()).ifPresent(player -> {
            String server = nucleoOnlinePlayerConnectRequestPacket.server();
            proxyServer.getServer(server).ifPresentOrElse(
                    registeredServer -> connectPlayer(player, nucleoOnlinePlayer, registeredServer, message),
                    () -> reply(message, new NucleoOnlinePlayerConnectResponsePacket(
                            nucleoOnlinePlayer,
                            server,
                            State.SERVER_NOT_FOUND
                    ))
            );
        });
    }

    private void connectPlayer(
            Player player,
            NucleoOnlinePlayer nucleoOnlinePlayer,
            RegisteredServer registeredServer,
            Message message
    ) {
        player.createConnectionRequest(registeredServer).connect().whenCompleteAsync(
                (result, throwable) -> {
                    String serverName = registeredServer.getServerInfo().getName();
                    State state = State.from(result.getStatus().name());
                    if (throwable != null) {
                        reply(message, new NucleoOnlinePlayerConnectResponsePacket(
                                nucleoOnlinePlayer,
                                serverName,
                                state
                        ));
                        return;
                    }
                    reply(message, new NucleoOnlinePlayerConnectResponsePacket(
                            nucleoOnlinePlayer,
                            serverName,
                            state
                    ));
                }
        );
    }
}