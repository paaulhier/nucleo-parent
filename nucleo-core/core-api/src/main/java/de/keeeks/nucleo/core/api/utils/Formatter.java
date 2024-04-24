package de.keeeks.nucleo.core.api.utils;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static net.kyori.adventure.text.Component.*;

public class Formatter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyyy - HH:mm:ss.SSS"
    ).withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter shortDateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyyy - HH:mm"
    ).withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm:ss.SSS"
    ).withZone(ZoneId.systemDefault());

    public static String formatInstant(Instant instant) {
        return dateTimeFormatter.format(instant);
    }

    public static String format(long millis) {
        return formatInstant(Instant.ofEpochMilli(millis));
    }

    private static final NumberFormat numberFormat = new DecimalFormat("00");
    private static final String SINGLE_SPACE = " ";

    public static String formatDateTime(Instant instant) {
        return dateTimeFormatter.format(instant);
    }

    public static String formatShortDateTime(Instant instant) {
        return shortDateTimeFormatter.format(instant);
    }

    public static String formatTime(Instant instant) {
        return timeFormatter.format(instant);
    }

    public static String formatShortenedTime(long millis) {
        return formatTime(millis, true);
    }

    public static String formatLongTime(long millis) {
        return formatTime(millis, false);
    }

    public static String formatTime(long millis, boolean shortened) {
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int years = 0;

        while (millis / 1000 > 0) {
            seconds++;
            millis -= 1000;
        }

        while (seconds / 60 > 0) {
            minutes++;
            seconds -= 60;
        }

        while (minutes / 60 > 0) {
            hours++;
            minutes -= 60;
        }

        while (hours / 24 > 0) {
            days++;
            hours -= 24;
        }

        while (days / 365 > 0) {
            years++;
            days -= 365;
        }

        StringBuilder timeBuilder = new StringBuilder();

        if (shortened) {
            return buildShortenedTime(
                    timeBuilder,
                    years,
                    days,
                    hours,
                    minutes,
                    seconds
            );
        }

        return buildLongTime(timeBuilder, years, days, hours, minutes, seconds);
    }

    @NotNull
    private static String buildLongTime(
            StringBuilder timeBuilder,
            int years,
            int days,
            int hours,
            int minutes,
            int seconds
    ) {
        if (years > 0) {
            timeBuilder.append(numberFormat.format(years))
                    .append("y")
                    .append(SINGLE_SPACE);
        }

        if (days > 0) {
            timeBuilder.append(numberFormat.format(days))
                    .append("d")
                    .append(SINGLE_SPACE);
        }

        if (hours > 0) {
            timeBuilder.append(numberFormat.format(hours))
                    .append("h")
                    .append(SINGLE_SPACE);
        }

        if (minutes > 0) {
            timeBuilder.append(numberFormat.format(minutes))
                    .append("m")
                    .append(SINGLE_SPACE);
        }

        return timeBuilder.append(numberFormat.format(seconds)).append("s").toString();
    }

    private static String buildShortenedTime(
            StringBuilder stringBuilder,
            int years,
            int days,
            int hours,
            int minutes,
            int seconds
    ) {
        if (years > 0) {
            return stringBuilder.append(years)
                    .append("y")
                    .append(SINGLE_SPACE)
                    .append(days)
                    .append("d")
                    .toString();
        }
        if (days > 0) {
            return stringBuilder.append(days)
                    .append("d")
                    .append(SINGLE_SPACE)
                    .append(hours)
                    .append("h")
                    .toString();
        }
        if (hours > 0) {
            return stringBuilder.append(hours)
                    .append("h")
                    .append(SINGLE_SPACE)
                    .append(minutes)
                    .append("m")
                    .toString();
        }
        if (minutes > 0) {
            return stringBuilder.append(minutes)
                    .append("m")
                    .append(SINGLE_SPACE)
                    .append(seconds)
                    .append("s")
                    .toString();
        }
        return stringBuilder.append(seconds)
                .append("s")
                .toString();
    }

    public static Component shortenedDuration(Duration duration) {
        return duration(duration, true);
    }

    public static Component duration(Duration duration) {
        return duration(duration, false);
    }

    public static Component duration(Duration duration, boolean shortened) {
        if (duration.isNegative() || duration.isZero()) {
            return translatable("duration.permanent");
        }

        long millis = duration.toMillis();
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long years = 0;

        while (millis >= 1000) {
            seconds++;
            millis -= 1000;
        }

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        while (hours >= 24) {
            days++;
            hours -= 24;
        }

        while (days >= 365) {
            years++;
            days -= 365;
        }

        Component component = empty();

        if (shortened) return buildShortenedDuration(
                component,
                years,
                days,
                hours,
                minutes,
                seconds
        );

        return buildLongDuration(
                component,
                years,
                days,
                hours,
                minutes,
                seconds
        );
    }

    private static Component buildLongDuration(
            Component component,
            long years,
            long days,
            long hours,
            long minutes,
            long seconds
    ) {
        if (years > 0) {
            component = component.append(text(years)).append(
                    text("y")
            ).append(space());
        }

        if (days > 0) {
            component = component.append(text(days)).append(
                    text("d")
            ).append(space());
        }

        if (hours > 0) {
            component = component.append(text(hours)).append(
                    text("h")
            ).append(space());
        }

        if (minutes > 0) {
            component = component.append(text(minutes)).append(
                    text("m")
            ).append(space());
        }

        if (seconds > 0) {
            component = component.append(text(seconds)).append(
                    text("s")
            ).append(space());
        }

        if (component.equals(empty())) {
            component = component.append(text(0)).append(
                    text("s")
            ).append(space());
        }
        return component;
    }

    private static Component buildShortenedDuration(
            Component component,
            long years,
            long days,
            long hours,
            long minutes,
            long seconds
    ) {
        if (years > 0) {
            component = component.append(text(years)).append(
                    text("y")
            ).append(space());
            return component;
        }

        if (days > 0) {
            component = component.append(text(days)).append(
                    text("d")
            ).append(space());
            return component;
        }

        if (hours > 0) {
            component = component.append(text(hours)).append(
                    text("h")
            ).append(space());
            return component;
        }

        if (minutes > 0) {
            component = component.append(text(minutes)).append(
                    text("m")
            ).append(space());
            return component;
        }

        if (seconds > 0) {
            component = component.append(text(seconds)).append(
                    text("s")
            ).append(space());
            return component;
        }

        if (component.equals(empty())) {
            component = component.append(text(0)).append(
                    text("s")
            ).append(space());
            return component;
        }
        return component;
    }
}