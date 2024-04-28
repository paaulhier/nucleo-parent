package de.keeeks.nucleo.modules.players.shared.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.api.Skin;
import de.keeeks.nucleo.modules.players.api.comment.Comment;
import de.keeeks.nucleo.modules.players.shared.DefaultNucleoPlayer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class NucleoPlayerSerializer extends JsonSerializer<NucleoPlayer> {
    private final Type commentType = new TypeToken<List<Comment>>() {}.getType();

    public NucleoPlayerSerializer() {
        super(NucleoPlayer.class, DefaultNucleoPlayer.class);
    }

    @Override
    public NucleoPlayer deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        DefaultNucleoPlayer nucleoPlayer = new DefaultNucleoPlayer(
                readOrNull(
                        jsonObject,
                        "uuid",
                        jsonElement1 -> UUID.fromString(jsonElement1.getAsString())
                ),
                readOrNull(
                        jsonObject,
                        "name",
                        JsonElement::getAsString
                ),
                jsonDeserializationContext.deserialize(
                        jsonObject.get("skin"),
                        Skin.class
                ),
                readOrNull(
                        jsonObject,
                        "lastIpAddress",
                        JsonElement::getAsString
                ),
                readOrNull(
                        jsonObject,
                        "onlineTime",
                        JsonElement::getAsLong
                ),
                jsonDeserializationContext.deserialize(jsonObject.get("comments"), commentType),
                readOrNull(
                        jsonObject,
                        "lastLogin",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                ),
                readOrNull(
                        jsonObject,
                        "lastLogout",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                ),
                readOrNull(
                        jsonObject,
                        "createdAt",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                ),
                readOrNull(
                        jsonObject,
                        "updatedAt",
                        jsonElement1 -> Instant.ofEpochMilli(jsonElement1.getAsLong())
                )
        );

        PropertyHolder propertyHolder = jsonDeserializationContext.deserialize(
                jsonObject.get("properties"),
                PropertyHolder.class
        );
        if (propertyHolder != null) {
            nucleoPlayer.properties().setProperties(propertyHolder);
        }

        return nucleoPlayer;
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
        jsonObject.add("lastIpAddress", new JsonPrimitive(nucleoPlayer.lastIpAddress()));
        jsonObject.addProperty("onlineTime", nucleoPlayer.onlineTime());
        jsonObject.add("comments", jsonSerializationContext.serialize(nucleoPlayer.comments(), commentType));
        jsonObject.add("properties", jsonSerializationContext.serialize(
                nucleoPlayer.properties(),
                PropertyHolder.class
        ));
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