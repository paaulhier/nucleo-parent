package de.keeeks.nucleo.modules.messaging.packet;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import io.nats.client.Message;
import lombok.Getter;

@Getter
public abstract class PacketListener<P extends Packet> {
    protected static final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    private final int priority;
    private final Class<P> packetClass;

    public PacketListener(Class<P> packetClass) {
        Class<? extends PacketListener<P>> clazz = (Class<? extends PacketListener<P>>) getClass();
        var listenerChannel = clazz.getAnnotation(ListenerChannel.class);
        if (listenerChannel == null) {
            throw new RuntimeException(
                    "Could not create an instance of %s. No ListenerChannel annotation present.".formatted(
                            clazz.getName()
                    )
            );
        }
        this.packetClass = packetClass;
        this.priority = listenerChannel.priority();
    }

    public abstract void receive(P p, Message message);

    public <R extends Packet> void reply(Message message, R replyPacket) {
        message.getConnection().publish(
                message.getReplyTo(),
                replyPacket.packetMeta().toBytes()
        );
    }

    public void receiveRaw(Object packet, Message message) {
        receive((P) packet, message);
    }
}