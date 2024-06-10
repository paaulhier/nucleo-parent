package de.keeeks.nucleo.core.api.utils;

public final class StringUtils {

    public static String replace(String text, String... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            text = text.replaceAll(replacements[i], replacements[i + 1]);
        }
        return text;
    }
}