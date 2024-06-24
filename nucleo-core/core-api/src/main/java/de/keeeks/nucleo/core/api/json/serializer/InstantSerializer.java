package de.keeeks.nucleo.core.api.json.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantSerializer extends JsonSerializer<Instant> {
    public InstantSerializer() {
        super(Instant.class);
    }

    @Override
    public Instant deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        if (jsonElement.isJsonNull()) return Instant.EPOCH;
        return Instant.ofEpochMilli(jsonElement.getAsLong());
    }

    @Override
    public JsonElement serialize(
            Instant instant,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return new JsonPrimitive(instant.toEpochMilli());
    }
}