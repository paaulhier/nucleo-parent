package de.keeeks.nucleo.modules.players.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.packet.player.NucleoOnlinePlayerUpdatePacket;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoOnlinePlayerUpdatePacketListener extends PacketListener<NucleoOnlinePlayerUpdatePacket> {
    private final PlayerService playerService;

    public NucleoOnlinePlayerUpdatePacketListener(PlayerService playerService) {
        super(NucleoOnlinePlayerUpdatePacket.class);
        this.playerService = playerService;
    }

    @Override
    public void receive(
            NucleoOnlinePlayerUpdatePacket nucleoOnlinePlayerUpdatePacket,
            Message message
    ) {
        playerService.updateCache(nucleoOnlinePlayerUpdatePacket.nucleoOnlinePlayer());
    }
}