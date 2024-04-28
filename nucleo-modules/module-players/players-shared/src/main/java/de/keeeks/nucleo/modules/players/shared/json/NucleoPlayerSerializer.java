package de.keeeks.nucleo.modules.players.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;

import java.lang.reflect.Type;

public class NucleoPlayerSerializer extends JsonSerializer<NucleoPlayer> {
    public NucleoPlayerSerializer() {
        super(NucleoPlayer.class);
    }

    @Override
    public NucleoPlayer deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                DefaultNucleoPlayer.class
        );
    }

    @Override
    public JsonElement serialize(
            NucleoPlayer nucleoPlayer,
            Type type,
            JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(
                nucleoPlayer,
                DefaultNucleoPlayer.class
        );
    }
}