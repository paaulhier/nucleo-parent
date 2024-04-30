package de.keeeks.nucleo.modules.translation.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.translation.shared.DefaultModuleDetails;
import de.keeeks.nucleo.modules.translations.api.ModuleDetails;

import java.lang.reflect.Type;

public class ModuleDetailsSerializer extends JsonSerializer<ModuleDetails> {
    public ModuleDetailsSerializer() {
        super(ModuleDetails.class);
    }

    @Override
    public ModuleDetails deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                DefaultModuleDetails.class
        );
    }

    @Override
    public JsonElement serialize(
            ModuleDetails moduleDetails,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                moduleDetails,
                DefaultModuleDetails.class
        );
    }
}