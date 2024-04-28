package de.keeeks.nucleo.modules.players.spigot.afk.configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

public record AFKConfiguration(
        boolean enabled,
        boolean showTitle,
        boolean sendMessage,
        List<TitleMessage> titleMessages,
        int titleChangeInterval,
        TimeUnit titleChangeIntervalUnit,
        int afkTime,
        TimeUnit afkTimeUnit
) {
    public static AFKConfiguration createDefault() {
        return new AFKConfiguration(
                true,
                true,
                true,
                List.of(
                        new TitleMessage("afk.title", "afk.title.subtitle")
                ),
                1,
                TimeUnit.MINUTES,
                5,
                TimeUnit.MINUTES
        );
    }
}