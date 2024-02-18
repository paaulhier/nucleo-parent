package de.keeeks.nucleo.modules.players.shared.json;

import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.Skin;

import java.lang.reflect.Type;
import java.util.UUID;

public class SkinSerializer extends JsonSerializer<Skin> {
    public SkinSerializer() {
        super(Skin.class);
    }

    @Override
    public Skin deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return new Skin(
                UUID.fromString(jsonElement.getAsJsonObject().get("playerId").getAsString()),
                jsonElement.getAsJsonObject().get("value").getAsString(),
                jsonElement.getAsJsonObject().get("signature").getAsString()
        );
    }

    @Override
    public JsonElement serialize(
            Skin skin,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("playerId", skin.playerId().toString());
        jsonObject.addProperty("value", skin.value());
        jsonObject.addProperty("signature", skin.signature());
        return jsonObject;
    }
}