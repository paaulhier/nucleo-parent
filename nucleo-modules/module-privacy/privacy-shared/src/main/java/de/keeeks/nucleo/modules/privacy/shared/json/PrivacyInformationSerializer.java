package de.keeeks.nucleo.modules.privacy.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;
import de.keeeks.nucleo.modules.privacy.shared.NucleoPrivacyInformation;

import java.lang.reflect.Type;

public class PrivacyInformationSerializer extends JsonSerializer<PrivacyInformation> {
    public PrivacyInformationSerializer() {
        super(PrivacyInformation.class);
    }

    @Override
    public PrivacyInformation deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                NucleoPrivacyInformation.class
        );
    }

    @Override
    public JsonElement serialize(
            PrivacyInformation privacyInformation,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                privacyInformation,
                NucleoPrivacyInformation.class
        );
    }
}