package de.keeeks.nucleo.modules.translation.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationEntry;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;

import java.lang.reflect.Type;

public class TranslationEntrySerializer extends JsonSerializer<TranslationEntry> {
    public TranslationEntrySerializer() {
        super(TranslationEntry.class);
    }

    @Override
    public TranslationEntry deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                DefaultTranslationEntry.class
        );
    }

    @Override
    public JsonElement serialize(
            TranslationEntry translationEntry,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                translationEntry,
                DefaultTranslationEntry.class
        );
    }
}