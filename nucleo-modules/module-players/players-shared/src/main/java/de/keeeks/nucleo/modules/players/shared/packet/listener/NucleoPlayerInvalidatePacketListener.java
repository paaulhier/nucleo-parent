package de.keeeks.nucleo.modules.players.shared.packet.listener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.shared.packet.player.NucleoPlayerInvalidatePacket;
import io.nats.client.Message;

@ListenerChannel(PlayerService.CHANNEL)
public class NucleoPlayerInvalidatePacketListener extends PacketListener<NucleoPlayerInvalidatePacket> {
    private final PlayerService playerService;

    public NucleoPlayerInvalidatePacketListener(PlayerService playerService) {
        super(NucleoPlayerInvalidatePacket.class);
        this.playerService = playerService;
    }

    @Override
    public void receive(
            NucleoPlayerInvalidatePacket nucleoPlayerInvalidatePacket,
            Message message
    ) {
        playerService.invalidateCache(nucleoPlayerInvalidatePacket.uuid());
    }
}