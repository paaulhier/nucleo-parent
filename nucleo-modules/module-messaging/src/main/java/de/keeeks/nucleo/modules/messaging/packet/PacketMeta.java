package de.keeeks.nucleo.modules.messaging.packet;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@Getter
public class PacketMeta {
    private static final Supplier<Gson> gson = GsonBuilder::globalGson;
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
            throw new RuntimeException(e);
        }
    }

    public static PacketMeta fromJson(String json) {
        return gson.get().fromJson(json, PacketMeta.class);
    }
}