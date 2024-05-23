package de.keeeks.nucleo.modules.vanish.shared;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.utils.ListModifier;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.api.VanishData;
import de.keeeks.nucleo.modules.vanish.api.packet.VanishDataInvalidatePacket;
import de.keeeks.nucleo.modules.vanish.shared.packetlistener.VanishDataInvalidatePacketListener;
import de.keeeks.nucleo.modules.vanish.shared.packetlistener.VanishDataUpdatePacketListener;
import de.keeeks.nucleo.modules.vanish.shared.serializer.VanishDataSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NucleoVanishApi implements VanishApi {
    private static final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    private final List<VanishData> vanishData = new ArrayList<>();

    public NucleoVanishApi() {
        GsonBuilder.registerSerializer(new VanishDataSerializer());
        natsConnection.registerPacketListener(
                new VanishDataUpdatePacketListener(this),
                new VanishDataInvalidatePacketListener(this)
        );
    }

    public void modifyVanishData(ListModifier<VanishData> modifier) {
        modifier.modify(vanishData);
    }

    @Override
    public VanishData vanishData(UUID uuid) {
        return vanishData.stream()
                .filter(vanishData -> vanishData.uuid().equals(uuid))
                .findFirst()
                .orElseGet(() -> new NucleoVanishData(uuid, false));
    }

    @Override
    public void invalidate(UUID uuid) {
        natsConnection.publishPacket(
                CHANNEL,
                new VanishDataInvalidatePacket(vanishData(uuid))
        );
    }
}