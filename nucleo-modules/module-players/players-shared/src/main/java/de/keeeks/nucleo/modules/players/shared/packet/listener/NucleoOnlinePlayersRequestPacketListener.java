package de.keeeks.nucleo.modules.players.shared.packet.listener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.packet.player.NucleoOnlinePlayersRequestPacket;
import de.keeeks.nucleo.modules.players.shared.packet.player.NucleoOnlinePlayersResponsePacket;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoOnlinePlayersRequestPacketListener extends PacketListener<NucleoOnlinePlayersRequestPacket> {
    private final PlayerService playerService;

    public NucleoOnlinePlayersRequestPacketListener(PlayerService playerService) {
        super(NucleoOnlinePlayersRequestPacket.class);
        this.playerService = playerService;
    }

    @Override
    public void receive(
            NucleoOnlinePlayersRequestPacket nucleoOnlinePlayersRequestPacket,
            Message message
    ) {
        reply(
                message,
                new NucleoOnlinePlayersResponsePacket(playerService.onlinePlayers())
        );
    }
}