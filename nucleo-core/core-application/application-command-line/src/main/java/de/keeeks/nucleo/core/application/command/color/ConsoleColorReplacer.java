package de.keeeks.nucleo.core.application.console.color;

import java.util.HashMap;
import java.util.Map;

public final class ConsoleColorReplacer {
    private static final Map<Character, Color> charColorMap = new HashMap<>() {{
        put('0', Color.BLACK);
        put('1', Color.BLUE);
        put('2', Color.GREEN);
        put('3', Color.CYAN_BRIGHT);
        put('4', Color.RED);
        put('5', Color.MAGENTA_BRIGHT);
        put('6', Color.YELLOW);
        put('7', Color.WHITE);
        put('8', Color.BLACK_BRIGHT);
        put('9', Color.BLUE_BRIGHT);
        put('a', Color.GREEN_BRIGHT);
        put('b', Color.CYAN_BRIGHT);
        put('c', Color.RED_BRIGHT);
        put('d', Color.MAGENTA_BRIGHT);
        put('e', Color.YELLOW);
        put('f', Color.WHITE);
        put('r', Color.RESET);
    }};

    public static String replaceCommonColorCodesForFileFormat(String text) {
        for (Character c : charColorMap.keySet()) {
            text = text.replaceAll(
                    "&" + c,
                    ""
            );
        }
        return text;
    }

    public static String replaceCommonColors(String text) {
        for (Map.Entry<Character, Color> entry : charColorMap.entrySet()) {
            text = text.replaceAll("&" + entry.getKey(), entry.getValue().toString());
        }
        return text;
    }
}