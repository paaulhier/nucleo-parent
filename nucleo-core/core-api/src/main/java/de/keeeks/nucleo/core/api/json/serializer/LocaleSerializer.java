package de.keeeks.nucleo.core.api.json.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public class LocaleSerializer extends JsonSerializer<Locale> {
    private static final String underscore = "_";

    public LocaleSerializer() {
        super(Locale.class);
    }

    @Override
    public Locale deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        var localeString = jsonElement.getAsJsonPrimitive().getAsString();
        var localeParts = localeString.split(underscore);

        if (localeParts.length == 1) {
            return Locale.of(localeParts[0]);
        }
        if (localeParts.length == 2) {
            return Locale.of(
                    localeParts[0],
                    localeParts[1]
            );
        }
        if (localeParts.length == 3) {
            return Locale.of(
                    localeParts[0],
                    localeParts[1],
                    localeParts[2]
            );
        }
        return Locale.ENGLISH;
    }

    @Override
    public JsonElement serialize(
            Locale locale,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        var localeStringBuilder = new StringBuilder();
        localeStringBuilder.append(locale.getLanguage());

        if (!locale.getCountry().isEmpty()) {
            localeStringBuilder.append(
                    underscore
            ).append(
                    locale.getCountry()
            );
        }

        if (!locale.getVariant().isEmpty()) {
            localeStringBuilder.append(
                    underscore
            ).append(
                    locale.getVariant()
            );
        }

        return new JsonPrimitive(
                localeStringBuilder.toString()
        );
    }
}