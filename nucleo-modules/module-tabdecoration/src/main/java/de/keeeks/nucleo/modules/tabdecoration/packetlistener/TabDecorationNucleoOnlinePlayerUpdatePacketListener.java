package de.keeeks.nucleo.modules.tabdecoration.packetlistener;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.packet.NucleoOnlinePlayerUpdatePacket;
import de.keeeks.nucleo.modules.tabdecoration.service.TabDecorationService;
import io.nats.client.Message;
import org.bukkit.Bukkit;

@ListenerChannel(PlayerService.CHANNEL)
public final class TabDecorationNucleoOnlinePlayerUpdatePacketListener extends PacketListener<NucleoOnlinePlayerUpdatePacket> {
    private final TabDecorationService tabDecorationService = ServiceRegistry.service(TabDecorationService.class);

    public TabDecorationNucleoOnlinePlayerUpdatePacketListener() {
        super(NucleoOnlinePlayerUpdatePacket.class);
    }

    @Override
    public void receive(
            NucleoOnlinePlayerUpdatePacket nucleoOnlinePlayerUpdatePacket,
            Message message
    ) {
        Bukkit.getOnlinePlayers().forEach(tabDecorationService::sendPlayerListHeaderAndFooter);
    }
}