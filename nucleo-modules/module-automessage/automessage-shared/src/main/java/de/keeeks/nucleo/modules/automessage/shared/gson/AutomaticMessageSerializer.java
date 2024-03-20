package de.keeeks.nucleo.modules.automessage.shared.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessage;

import java.lang.reflect.Type;

public class AutomaticMessageSerializer extends JsonSerializer<AutomaticMessage> {

    public AutomaticMessageSerializer() {
        super(AutomaticMessage.class);
    }

    @Override
    public AutomaticMessage deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                NucleoAutomaticMessage.class
        );
    }

    @Override
    public JsonElement serialize(AutomaticMessage automaticMessage, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(
                automaticMessage,
                NucleoAutomaticMessage.class
        );
    }
}