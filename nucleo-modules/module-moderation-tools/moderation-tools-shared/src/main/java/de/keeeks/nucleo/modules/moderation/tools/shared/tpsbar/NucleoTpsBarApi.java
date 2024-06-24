package de.keeeks.nucleo.modules.moderation.tools.shared.tpsbar;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.ListModifier;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener.tpsbar.TpsBarStatesRequestPacketListener;
import de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener.tpsbar.TpsBarUpdateStatePacketListener;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.TpsBarApi;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet.TpsBarDisablePacket;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet.TpsBarEnablePacket;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet.TpsBarStatesRequestPacket;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet.TpsBarStatesResponsePacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NucleoTpsBarApi implements TpsBarApi {
    private final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);
    protected final List<UUID> enabledPlayers = new ArrayList<>();

    public NucleoTpsBarApi() {
        natsConnection.request(
                CHANNEL,
                new TpsBarStatesRequestPacket(),
                TpsBarStatesResponsePacket.class
        ).whenCompleteAsync((tpsBarStatesResponsePacket, throwable) -> {
            if (tpsBarStatesResponsePacket != null) {
                enabledPlayers.addAll(tpsBarStatesResponsePacket.enabledPlayers());
            }
        });
        natsConnection.registerPacketListener(
                new TpsBarStatesRequestPacketListener(this),
                new TpsBarUpdateStatePacketListener(this)
        );
    }

    public void modifyEnabledPlayers(ListModifier<UUID> modifier) {
        modifier.modify(enabledPlayers);
    }

    public final List<UUID> enabledPlayers() {
        return List.copyOf(enabledPlayers);
    }

    @Override
    public boolean enabled(UUID uuid) {
        return enabledPlayers().contains(uuid);
    }

    @Override
    public void enabled(UUID uuid, boolean enabled) {
        if (enabled) {
            enabledPlayers.add(uuid);
        } else {
            enabledPlayers.remove(uuid);
        }

        Packet packet;

        if (enabled) {
            packet = new TpsBarEnablePacket(uuid);
        } else {
            packet = new TpsBarDisablePacket(uuid);
        }

        natsConnection.publishPacket(CHANNEL, packet);
    }
}