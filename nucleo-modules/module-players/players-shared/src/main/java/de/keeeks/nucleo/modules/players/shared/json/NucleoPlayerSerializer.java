package de.keeeks.nucleo.modules.players.shared.json;

import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.Skin;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.UUID;

public class NucleoPlayerSerializer extends JsonSerializer<NucleoPlayer> {

    public NucleoPlayerSerializer() {
        super(NucleoPlayer.class, DefaultNucleoPlayer.class);
    }

    @Override
    public NucleoPlayer deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return new DefaultNucleoPlayer(
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "uuid",
                        jsonElement1 -> UUID.fromString(jsonElement1.getAsString())
                ),
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "name",
                        JsonElement::getAsString
                ),
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "locale",
                        jsonElement1 -> jsonDeserializationContext.deserialize(jsonElement1, java.util.Locale.class)
                ),
                jsonDeserializationContext.deserialize(
                        jsonElement.getAsJsonObject().get("skin"),
                        Skin.class
                ),
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "onlineTime",
                        JsonElement::getAsLong
                ),
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "lastLogin",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                ),
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "lastLogout",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                ),
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "createdAt",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                ),
                readOrNull(
                        jsonElement.getAsJsonObject(),
                        "updatedAt",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                )
        );
    }

    @Override
    public JsonElement serialize(
            NucleoPlayer nucleoPlayer,
            Type type,
            JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", nucleoPlayer.uuid().toString());
        jsonObject.addProperty("name", nucleoPlayer.name());
        jsonObject.add("skin", jsonSerializationContext.serialize(nucleoPlayer.skin()));
        jsonObject.add("locale", jsonSerializationContext.serialize(nucleoPlayer.locale()));
        jsonObject.addProperty("onlineTime", nucleoPlayer.onlineTime());
        jsonObject.add(
                "lastLogin",
                nucleoPlayer.lastLogin() == null ? JsonNull.INSTANCE : new JsonPrimitive(
                        nucleoPlayer.lastLogin().toEpochMilli()
                )
        );
        jsonObject.add(
                "lastLogout",
                nucleoPlayer.lastLogout() == null ? JsonNull.INSTANCE : new JsonPrimitive(
                        nucleoPlayer.lastLogout().toEpochMilli()
                )
        );
        jsonObject.addProperty("createdAt", nucleoPlayer.createdAt().toEpochMilli());
        jsonObject.addProperty("updatedAt", nucleoPlayer.updatedAt().toEpochMilli());
        return jsonObject;
    }
}