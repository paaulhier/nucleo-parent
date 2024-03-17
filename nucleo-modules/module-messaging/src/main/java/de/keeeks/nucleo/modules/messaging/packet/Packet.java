package de.keeeks.nucleo.modules.messaging.packet;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import lombok.Getter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Getter
public abstract class Packet {
    private static final Gson gson = GsonBuilder.globalGson();

    private final String id = packetNameToHex();

    private String packetNameToHex() {
        return String.format("%x", new BigInteger(
                1,
                getClass().getName().getBytes(StandardCharsets.UTF_8)
        ));
    }

    public PacketMeta packetMeta() {
        return new PacketMeta(this);
    }
}