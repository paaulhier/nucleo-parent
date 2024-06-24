package de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.ping;

import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.ping.PlayerPingRequestPacket;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.ping.PlayerPingResponsePacket;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import io.nats.client.Message;

@ListenerChannel("common-commands")
public class PlayerPingRequestPacketListener extends PacketListener<PlayerPingRequestPacket> {
    private final VelocityModule module;

    public PlayerPingRequestPacketListener(VelocityModule module) {
        super(PlayerPingRequestPacket.class);
        this.module = module;
    }

    @Override
    public void receive(
            PlayerPingRequestPacket playerPingRequestPacket,
            Message message
    ) {
        module.proxyServer().getPlayer(playerPingRequestPacket.uuid()).ifPresent(
                player -> reply(message, new PlayerPingResponsePacket(
                        playerPingRequestPacket.uuid(),
                        (int) player.getPing()
                ))
        );
    }
}