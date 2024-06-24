package de.keeeks.nucleo.core.api.json.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.regex.Pattern;

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
        if (localeString == null) return Locale.ENGLISH;

        var localeParts = localeString.split(underscore);

        return Locale.of(
                stringOrIfNotPresentEmpty(localeParts, 0),
                stringOrIfNotPresentEmpty(localeParts, 1),
                stringOrIfNotPresentEmpty(localeParts, 2)
        );
    }

    @Override
    public JsonElement serialize(
            Locale locale,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return new JsonPrimitive("%s_%s_%s".formatted(
                locale.getLanguage(),
                locale.getCountry(),
                locale.getVariant()
        ));
    }

    private String stringOrIfNotPresentEmpty(String[] parts, int index) {
        return parts.length > index ? parts[index] : "";
    }
}