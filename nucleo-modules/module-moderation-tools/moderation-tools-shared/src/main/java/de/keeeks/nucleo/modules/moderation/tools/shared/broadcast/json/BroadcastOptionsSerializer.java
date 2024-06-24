package de.keeeks.nucleo.modules.moderation.tools.shared.broadcast.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastOptions;
import de.keeeks.nucleo.modules.moderation.tools.shared.broadcast.NucleoBroadcastOptions;

import java.lang.reflect.Type;

public class BroadcastOptionsSerializer extends JsonSerializer<BroadcastOptions> {
    public BroadcastOptionsSerializer() {
        super(BroadcastOptions.class);
    }

    @Override
    public BroadcastOptions deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                NucleoBroadcastOptions.class
        );
    }

    @Override
    public JsonElement serialize(
            BroadcastOptions broadcastOptions,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                broadcastOptions,
                NucleoBroadcastOptions.class
        );
    }
}