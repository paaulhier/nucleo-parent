package de.keeeks.nucleo.core.api.utils;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Formatter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyyy - HH.mm.ss.SSS"
    ).withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(
            "HH.mm.ss.SSS"
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
}