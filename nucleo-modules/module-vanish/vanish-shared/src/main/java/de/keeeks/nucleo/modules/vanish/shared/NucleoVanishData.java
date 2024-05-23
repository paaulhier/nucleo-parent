package de.keeeks.nucleo.modules.vanish.shared;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.vanish.api.VanishApi;
import de.keeeks.nucleo.modules.vanish.api.VanishData;
import de.keeeks.nucleo.modules.vanish.api.packet.VanishDataUpdatePacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class NucleoVanishData implements VanishData {
    private static final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    private final UUID uuid;
    private boolean vanished;

    @Override
    public boolean vanished(boolean vanished) {
        this.vanished = vanished;
        natsConnection.publishPacket(
                VanishApi.CHANNEL,
                new VanishDataUpdatePacket(this)
        );
        return this.vanished;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NucleoVanishData that = (NucleoVanishData) object;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}