package de.keeeks.nucleo.modules.players.shared.json;

import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.Version;

import java.lang.reflect.Type;

public final class VersionSerializer extends JsonSerializer<Version> {

    public VersionSerializer() {
        super(Version.class);
    }

    @Override
    public Version deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String versionAsString = json.getAsJsonPrimitive().getAsString();
        Version version = Version.getByName(versionAsString);
        if (version == null) return Version.UNKNOWN;
        return version;
    }

    @Override
    public JsonElement serialize(Version src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name());
    }
}