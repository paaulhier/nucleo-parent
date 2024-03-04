package de.keeeks.nucleo.modules.players.shared.json;


import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.Version;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;

import java.lang.reflect.Type;

public class NucleoOnlinePlayerSerializer extends JsonSerializer<NucleoOnlinePlayer> {

    public NucleoOnlinePlayerSerializer() {
        super(NucleoOnlinePlayer.class, DefaultNucleoOnlinePlayer.class);
    }

    @Override
    public JsonElement serialize(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        var jsonObject = jsonSerializationContext.serialize(
                nucleoOnlinePlayer,
                DefaultNucleoPlayer.class
        ).getAsJsonObject();
        jsonObject.addProperty("ipAddress", nucleoOnlinePlayer.ipAddress());
        jsonObject.addProperty("proxy", nucleoOnlinePlayer.proxy());
        jsonObject.addProperty("server", nucleoOnlinePlayer.server());
        jsonObject.addProperty("version", nucleoOnlinePlayer.version().protocol());
        return jsonObject;
    }

    @Override
    public NucleoOnlinePlayer deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement serverElement = jsonObject.get("server");
        return new DefaultNucleoOnlinePlayer(
                jsonDeserializationContext.deserialize(jsonElement, DefaultNucleoPlayer.class),
                jsonObject.get("proxy").getAsString(),
                serverElement == null ? null : serverElement.getAsString(),
                jsonObject.get("ipAddress").getAsString(),
                Version.byProtocol(jsonObject.get("version").getAsInt())
        );
    }
}