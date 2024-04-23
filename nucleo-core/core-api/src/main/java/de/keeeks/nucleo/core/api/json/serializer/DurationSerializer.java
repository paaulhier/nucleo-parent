package de.keeeks.nucleo.core.api.json.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationSerializer extends JsonSerializer<Duration> {
    public DurationSerializer() {
        super(Duration.class);
    }

    @Override
    public Duration deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return Duration.ofMillis(jsonElement.getAsLong());
    }

    @Override
    public JsonElement serialize(
            Duration duration,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return new JsonPrimitive(duration.toMillis());
    }
}