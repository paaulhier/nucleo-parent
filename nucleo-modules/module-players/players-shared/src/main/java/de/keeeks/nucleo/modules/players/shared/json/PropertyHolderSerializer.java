package de.keeeks.nucleo.modules.players.shared.json;

import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.shared.DefaultPropertyHolder;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

public class PropertyHolderSerializer extends JsonSerializer<PropertyHolder> {

    public PropertyHolderSerializer() {
        super(PropertyHolder.class, DefaultPropertyHolder.class);
    }

    @SneakyThrows
    @Override
    public PropertyHolder deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        DefaultPropertyHolder propertyHolder = new DefaultPropertyHolder();

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String key = jsonObject.get("key").getAsString();
            JsonElement value = jsonObject.get("value");
            Class<?> typeClass = Class.forName(
                    jsonObject.get("type").getAsString()
            );
            propertyHolder.setProperty(key, jsonDeserializationContext.deserialize(value, typeClass));
        }

        return propertyHolder;
    }

    @Override
    public JsonElement serialize(
            PropertyHolder propertyHolder,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        JsonArray jsonArray = new JsonArray();

        for (String key : propertyHolder.keys()) {
            propertyHolder.optionalProperty(key).ifPresent(property -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("key", key);
                jsonObject.add("value", jsonSerializationContext.serialize(property));
                jsonObject.addProperty("type", property.getClass().getName());
                jsonArray.add(jsonObject);
            });
        }

        return jsonArray;
    }
}