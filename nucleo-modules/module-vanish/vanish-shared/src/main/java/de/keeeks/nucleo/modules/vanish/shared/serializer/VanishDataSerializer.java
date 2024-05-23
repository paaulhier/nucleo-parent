package de.keeeks.nucleo.modules.vanish.shared.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.vanish.api.VanishData;
import de.keeeks.nucleo.modules.vanish.shared.NucleoVanishData;

import java.lang.reflect.Type;

public class VanishDataSerializer extends JsonSerializer<VanishData> {
    public VanishDataSerializer() {
        super(VanishData.class);
    }

    @Override
    public VanishData deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                NucleoVanishData.class
        );
    }

    @Override
    public JsonElement serialize(
            VanishData vanishData,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                vanishData,
                NucleoVanishData.class
        );
    }
}