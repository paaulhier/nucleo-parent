package de.keeeks.nucleo.modules.players.shared.json;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoOnlinePlayer;

import java.lang.reflect.Type;

public class NucleoOnlinePlayerSerializer extends JsonSerializer<NucleoOnlinePlayer> {

    public NucleoOnlinePlayerSerializer() {
        super(NucleoOnlinePlayer.class);
    }

    @Override
    public JsonElement serialize(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                nucleoOnlinePlayer,
                DefaultNucleoOnlinePlayer.class
        );
    }

    @Override
    public NucleoOnlinePlayer deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                DefaultNucleoOnlinePlayer.class
        );
    }
}