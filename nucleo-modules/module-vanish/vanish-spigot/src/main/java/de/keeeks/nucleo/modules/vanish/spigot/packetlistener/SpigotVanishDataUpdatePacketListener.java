package de.keeeks.nucleo.modules.vanish.spigot.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.ListenerOrder;
import de.keeeks.nucleo.modules.messaging.packet.Order;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.api.VanishData;
import de.keeeks.nucleo.modules.vanish.api.packet.VanishDataUpdatePacket;
import de.keeeks.nucleo.modules.vanish.spigot.event.PlayerToggleVanishEvent;
import io.nats.client.Message;
import org.bukkit.Bukkit;

import java.util.Optional;

@Order(ListenerOrder.LAST)
@ListenerChannel(VanishApi.CHANNEL)
public class SpigotVanishDataUpdatePacketListener extends PacketListener<VanishDataUpdatePacket> {
    public SpigotVanishDataUpdatePacketListener() {
        super(VanishDataUpdatePacket.class);
    }

    @Override
    public void receive(
            VanishDataUpdatePacket vanishDataUpdatePacket,
            Message message
    ) {
        VanishData vanishData = vanishDataUpdatePacket.vanishData();

        Optional.ofNullable(Bukkit.getPlayer(vanishData.uuid())).ifPresent(
                player -> Bukkit.getPluginManager().callEvent(new PlayerToggleVanishEvent(
                        player,
                        vanishData
                ))
        );
    }
}