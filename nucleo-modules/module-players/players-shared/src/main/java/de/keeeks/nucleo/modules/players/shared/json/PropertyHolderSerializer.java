package de.keeeks.nucleo.modules.players.shared.json;

import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.shared.DefaultPropertyHolder;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

public class PropertyHolderSerializer extends JsonSerializer<PropertyHolder> {

    public PropertyHolderSerializer() {
        super(PropertyHolder.class);
    }

    @SneakyThrows
    @Override
    public PropertyHolder deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                DefaultPropertyHolder.class
        );
    }

    @Override
    public JsonElement serialize(
            PropertyHolder propertyHolder,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                propertyHolder,
                DefaultPropertyHolder.class
        );
    }
}