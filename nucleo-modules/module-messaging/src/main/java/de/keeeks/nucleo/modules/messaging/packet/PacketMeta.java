package de.keeeks.nucleo.modules.messaging.packet;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@Getter
public class PacketMeta {
    private static final Supplier<Gson> gson = GsonBuilder::globalGson;

    private final String sender = Module.serviceName();

    private final String packetJson;
    private final String className;

    public PacketMeta(Packet packet) {
        this.packetJson = gson.get().toJson(packet);
        this.className = packet.getClass().getName();
    }

    public byte[] toBytes() {
        return gson.get().toJson(this).getBytes(StandardCharsets.UTF_8);
    }

    public <P extends Packet> P packet() {
        try {
            return (P) gson.get().fromJson(packetJson, Class.forName(className));
        } catch (ClassNotFoundException e) {
            //The ClassNotFoundException is ok, because if a module sends this packet, but the module is
            //not on the receiver's classpath, the receiver will not be able to deserialize the packet.
            return null;
        } catch (Throwable throwable) {
            throw new RuntimeException("Error while deserializing packet", throwable);
        }
    }

    public static PacketMeta fromJson(String json) {
        return gson.get().fromJson(json, PacketMeta.class);
    }
}