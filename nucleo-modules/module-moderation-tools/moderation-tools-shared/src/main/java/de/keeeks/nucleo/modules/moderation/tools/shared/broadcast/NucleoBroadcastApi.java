package de.keeeks.nucleo.modules.moderation.tools.shared.broadcast;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastApi;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastOptions;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastOptionsBuilder;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.packet.BroadcastPacket;
import de.keeeks.nucleo.modules.moderation.tools.shared.broadcast.json.BroadcastOptionsSerializer;
import net.kyori.adventure.text.Component;

public class NucleoBroadcastApi implements BroadcastApi {
    private final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    public NucleoBroadcastApi() {
        GsonBuilder.registerSerializer(new BroadcastOptionsSerializer());
    }

    @Override
    public void broadcast(Component component) {
        broadcast(component, broadcastOptions -> NucleoBroadcastOptions.EMPTY);
    }

    @Override
    public void broadcast(Component component, BroadcastOptionsBuilder broadcastOptionsBuilder) {
        BroadcastOptions broadcastOptions = broadcastOptionsBuilder.build(NucleoBroadcastOptions.EMPTY);
        BroadcastPacket packet = new BroadcastPacket(component, broadcastOptions);
        natsConnection.publishPacket(CHANNEL, packet);
    }
}