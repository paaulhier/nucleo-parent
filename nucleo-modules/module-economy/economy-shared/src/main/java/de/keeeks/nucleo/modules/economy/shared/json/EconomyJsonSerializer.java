package de.keeeks.nucleo.modules.translation.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.translation.shared.NucleoEconomy;

import java.lang.reflect.Type;

public class EconomyJsonSerializer extends JsonSerializer<Economy> {

    public EconomyJsonSerializer() {
        super(Economy.class);
    }

    @Override
    public Economy deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                NucleoEconomy.class
        );
    }

    @Override
    public JsonElement serialize(
            Economy economy,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                economy,
                NucleoEconomy.class
        );
    }
}