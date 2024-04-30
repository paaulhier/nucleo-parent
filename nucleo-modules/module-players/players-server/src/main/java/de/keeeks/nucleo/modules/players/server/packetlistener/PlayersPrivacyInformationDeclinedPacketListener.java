package de.keeeks.nucleo.modules.players.server.packetlistener;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.api.packet.PrivacyInformationDeclinedPacket;
import io.nats.client.Message;

import java.util.logging.Logger;

@ListenerChannel(PrivacyApi.CHANNEL)
public class PlayersPrivacyInformationDeclinedPacketListener extends PacketListener<PrivacyInformationDeclinedPacket> {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final Logger logger = Module.module("players").logger();

    public PlayersPrivacyInformationDeclinedPacketListener() {
        super(PrivacyInformationDeclinedPacket.class);
    }

    @Override
    public void receive(PrivacyInformationDeclinedPacket privacyInformationDeclinedPacket, Message message) {
        logger.info("Received privacy information declined packet from player %s.".formatted(privacyInformationDeclinedPacket.playerId()));
        playerService.player(privacyInformationDeclinedPacket.playerId()).ifPresent(nucleoPlayer -> {
            logger.info("Player %s (%s) declined privacy information. Deleting player data.".formatted(
                    nucleoPlayer.name(),
                    nucleoPlayer.uuid()
            ));
            playerService.deletePlayer(nucleoPlayer.uuid());
        });
    }
}