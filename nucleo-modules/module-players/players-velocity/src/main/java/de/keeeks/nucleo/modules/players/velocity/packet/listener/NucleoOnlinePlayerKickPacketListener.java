package de.keeeks.nucleo.modules.players.velocity.packet.listener;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerKickPacket;
import io.nats.client.Message;

import static net.kyori.adventure.text.Component.translatable;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoOnlinePlayerKickPacketListener extends PacketListener<NucleoOnlinePlayerKickPacket> {
    private final ProxyServer proxyServer;

    public NucleoOnlinePlayerKickPacketListener(ProxyServer proxyServer) {
        super(NucleoOnlinePlayerKickPacket.class);
        this.proxyServer = proxyServer;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerKickPacket nucleoOnlinePlayerKickPacket,
            Message message
    ) {
        NucleoOnlinePlayer nucleoOnlinePlayer = nucleoOnlinePlayerKickPacket.player();
        proxyServer.getPlayer(nucleoOnlinePlayer.uuid()).ifPresent(player -> {
            if (nucleoOnlinePlayerKickPacket.raw()) {
                player.disconnect(nucleoOnlinePlayerKickPacket.reason());
                return;
            }
            player.disconnect(translatable(
                    "nucleo.players.kick",
                    nucleoOnlinePlayerKickPacket.reason()
            ));
        });
    }
}