package de.keeeks.nucleo.modules.players.velocity.packet.listener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.CommandTarget;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerExecuteCommandPacket;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class VelocityNucleoOnlinePlayerExecuteCommandPacketListener extends PacketListener<NucleoOnlinePlayerExecuteCommandPacket> {
    private final ProxyServer proxyServer;

    public VelocityNucleoOnlinePlayerExecuteCommandPacketListener(ProxyServer proxyServer) {
        super(NucleoOnlinePlayerExecuteCommandPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerExecuteCommandPacket nucleoOnlinePlayerExecuteCommandPacket,
            Message message
    ) {
        if (nucleoOnlinePlayerExecuteCommandPacket.commandTarget() != CommandTarget.PROXY) return;
        proxyServer.getPlayer(nucleoOnlinePlayerExecuteCommandPacket.player().uuid()).ifPresent(
                player -> proxyServer.getCommandManager().executeAsync(
                        player,
                        nucleoOnlinePlayerExecuteCommandPacket.command()
                )
        );
    }
}