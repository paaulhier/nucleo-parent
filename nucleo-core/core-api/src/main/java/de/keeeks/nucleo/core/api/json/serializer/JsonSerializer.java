package de.keeeks.nucleo.core.api.json.serializer;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Type;
import java.util.function.Function;

@Getter
@Accessors(fluent = true)
public abstract class JsonSerializer<T> implements com.google.gson.JsonSerializer<T>, JsonDeserializer<T> {
    private final Type[] types;

    public JsonSerializer(Type... types) {
        this.types = types;
    }

    protected <V> V readOrNull(JsonObject element, String key, Function<JsonElement, V> parser) {
        return element.has(key) ? parser.apply(element.get(key)) : null;
    }
}