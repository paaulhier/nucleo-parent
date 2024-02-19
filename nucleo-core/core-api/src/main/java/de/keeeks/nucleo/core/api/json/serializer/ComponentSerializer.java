package de.keeeks.nucleo.core.api.json.serializer;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;

public class ComponentSerializer extends JsonSerializer<Component> {
    private final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();

    @Override
    public Component deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return gsonComponentSerializer.deserialize(
                jsonElement.getAsString()
        );
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(gsonComponentSerializer.serialize(
                component
        ));
    }
}