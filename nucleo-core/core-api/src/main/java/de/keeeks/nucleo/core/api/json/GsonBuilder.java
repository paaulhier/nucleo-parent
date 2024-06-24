package de.keeeks.nucleo.core.api.json;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.serializer.*;
import de.keeeks.nucleo.core.api.utils.UpdateListener;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class GsonBuilder {
    private static final List<UpdateListener<Gson>> updateListener = new ArrayList<>();

    @Getter
    private static Gson globalGson = new com.google.gson.GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantSerializer())
            .registerTypeAdapter(Locale.class, new LocaleSerializer())
            .registerTypeAdapter(Component.class, new ComponentSerializer())
            .registerTypeAdapter(Duration.class, new DurationSerializer())
            .setPrettyPrinting()
            .create();

    @Deprecated
    public static void registerUpdateListener(UpdateListener<Gson> updateListener) {
        GsonBuilder.updateListener.add(updateListener);
    }

    public static Gson create(JsonSerializer<?>... jsonSerializers) {
        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder().setPrettyPrinting();
        registerSerializer(jsonSerializers, gsonBuilder);
        return gsonBuilder.create();
    }

    public static void registerSerializer(JsonSerializer<?>... jsonSerializers) {
        var gsonBuilder = globalGson.newBuilder();
        registerSerializer(jsonSerializers, gsonBuilder);
        globalGson = gsonBuilder.create();
        for (UpdateListener<Gson> gsonBuilderUpdateListener : updateListener) {
            gsonBuilderUpdateListener.update(globalGson);
        }
    }

    private static void registerSerializer(JsonSerializer<?>[] jsonSerializers, com.google.gson.GsonBuilder gsonBuilder) {
        for (JsonSerializer<?> jsonSerializer : jsonSerializers) {
            for (Type type : jsonSerializer.types()) {
                gsonBuilder.registerTypeAdapter(type, jsonSerializer);
            }
        }
    }
}