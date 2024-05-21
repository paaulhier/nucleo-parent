package de.keeeks.nucleo.modules.players.spigot.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.CommandTarget;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerExecuteCommandPacket;
import io.nats.client.Message;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@ListenerChannel(PlayerService.CHANNEL)
public class SpigotNucleoOnlinePlayerExecuteCommandPacketListener extends PacketListener<NucleoOnlinePlayerExecuteCommandPacket> {
    private final Server server = Bukkit.getServer();

    public SpigotNucleoOnlinePlayerExecuteCommandPacketListener() {
        super(NucleoOnlinePlayerExecuteCommandPacket.class);
    }

    @Override
    public void receive(
            NucleoOnlinePlayerExecuteCommandPacket nucleoOnlinePlayerExecuteCommandPacket,
            Message message
    ) {
        if (nucleoOnlinePlayerExecuteCommandPacket.commandTarget() != CommandTarget.SERVER) return;
        Player player = server.getPlayer(nucleoOnlinePlayerExecuteCommandPacket.player().uuid());
        if (player == null) return;

        server.dispatchCommand(
                player,
                nucleoOnlinePlayerExecuteCommandPacket.command()
        );
    }
}