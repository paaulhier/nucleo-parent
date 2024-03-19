package de.keeeks.nucleo.modules.common.commands.proxy.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record PushConfiguration(
        Environment environment,
        String url,
        String logoUrl,
        String content,
        String footerMessage
) {

    public static PushConfiguration createDefault() {
        return new PushConfiguration(
                Environment.DEV,
                "https://url.goes.here",
                "https://logo.url.goes.here",
                "Neue Änderungen wurden veröffentlicht.",
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