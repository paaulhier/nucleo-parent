package de.keeeks.nucleo.modules.players.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoPlayerUpdatePacket;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoPlayerUpdatePacketListener extends PacketListener<NucleoPlayerUpdatePacket> {
    private final PlayerService playerService;

    public NucleoPlayerUpdatePacketListener(PlayerService playerService) {
        super(NucleoPlayerUpdatePacket.class);
        this.playerService = playerService;
    }

    @Override
    public void receive(
            NucleoPlayerUpdatePacket nucleoPlayerUpdatePacket,
            Message message
    ) {
        playerService.updateCache(nucleoPlayerUpdatePacket.nucleoPlayer());
    }
}