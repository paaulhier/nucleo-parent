package de.keeeks.nucleo.modules.moderation.tools.velocity.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

public record PushConfiguration(
        Environment environment,
        String url,
        String logoUrl,
        String content,
        String color,
        String footerMessage
) {

    public Color colorByHex() {
        return new Color(
                Integer.valueOf(color.substring(1, 3), 16),
                Integer.valueOf(color.substring(3, 5), 16),
                Integer.valueOf(color.substring(5, 7), 16)
        );
    }

    public static PushConfiguration createDefault() {
        return new PushConfiguration(
                Environment.DEV,
                "https://url.goes.here",
                "https://logo.url.goes.here",
                "Neue Änderungen wurden veröffentlicht.",
                "#00ff00",
                "Änderungen können erst nach einem Neustart des Servers aktiv sein."
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Environment {
        PRD("Production"),
        DEV("Development");

        private final String display;
    }
}