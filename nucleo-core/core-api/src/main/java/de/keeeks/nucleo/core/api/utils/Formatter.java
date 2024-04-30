package de.keeeks.nucleo.core.api.utils;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern durationPattern = Pattern.compile(
            "(\\d+)([smhdy])"
    );
    private static final Pattern permanentPattern = Pattern.compile(
            "perm|perma|permanent|forever|-1"
    );

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

    public static Duration parseDuration(String time) {
        Matcher permanentMatcher = permanentPattern.matcher(time);
        Matcher durationMatcher = durationPattern.matcher(time);

        if (permanentMatcher.find()) {
            return Duration.ZERO;
        }

        if (!durationMatcher.find()) {
            return null;
        }

        int amount = Integer.parseInt(durationMatcher.group(1));
        String durationUnit = durationMatcher.group(2);

        return switch (durationUnit) {
            case "s" -> Duration.ofSeconds(amount);
            case "m" -> Duration.ofMinutes(amount);
            case "h" -> Duration.ofHours(amount);
            case "d" -> Duration.ofDays(amount);
            case "y" -> Duration.ofDays(amount * 365L);
            default -> Duration.ZERO;
        };
    }

    public static Component shortenedDuration(Duration duration) {
        return duration(duration, true, true);
    }

    public static Component duration(Duration duration) {
        return duration(duration, false, true);
    }

    public static Component duration(Duration duration, boolean shortened) {
        return duration(duration, shortened, true);
    }

    public static Component duration(Duration duration, boolean shortened, boolean shortenedUnits) {
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
                seconds,
                shortenedUnits
        );

        return buildLongDuration(
                component,
                years,
                days,
                hours,
                minutes,
                seconds,
                shortenedUnits
        );
    }

    private static Component buildLongDuration(
            Component component,
            long years,
            long days,
            long hours,
            long minutes,
            long seconds,
            boolean shortenedUnits
    ) {
        if (years > 0) {
            component = component.append(text(years)).append(
                    shortenedUnits ? text("y") : space().append(text("Years"))
            ).append(space());
        }

        if (days > 0) {
            component = component.append(text(days)).append(
                    shortenedUnits ? text("d") : space().append(text("Days"))
            ).append(space());
        }

        if (hours > 0) {
            component = component.append(text(hours)).append(
                    shortenedUnits ? text("h") : space().append(text("Hours"))
            ).append(space());
        }

        if (minutes > 0) {
            component = component.append(text(minutes)).append(
                    shortenedUnits ? text("m") : space().append(text("Minutes"))
            ).append(space());
        }

        if (seconds > 0) {
            component = component.append(text(seconds)).append(
                    shortenedUnits ? text("s") : space().append(text("Seconds"))
            ).append(space());
        }

        if (component.equals(empty())) {
            component = component.append(text(0)).append(
                    shortenedUnits ? text("s") : space().append(text("Seconds"))
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
            long seconds,
            boolean shortenedUnits
    ) {
        if (years > 0) {
            component = component.append(text(years)).append(
                    shortenedUnits ? text("y") : space().append(text("Years"))
            );
            return component;
        }

        if (days > 0) {
            component = component.append(text(days)).append(
                    shortenedUnits ? text("d") : space().append(text("Days"))
            );
            return component;
        }

        if (hours > 0) {
            component = component.append(text(hours)).append(
                    shortenedUnits ? text("h") : space().append(text("Hours"))
            );
            return component;
        }

        if (minutes > 0) {
            component = component.append(text(minutes)).append(
                    shortenedUnits ? text("m") : space().append(text("Minutes"))
            );
            return component;
        }

        if (seconds > 0) {
            component = component.append(text(seconds)).append(
                    shortenedUnits ? text("s") : space().append(text("Seconds"))
            );
            return component;
        }

        if (component.equals(empty())) {
            component = component.append(text(0)).append(
                    shortenedUnits ? text("s") : space().append(text("Seconds"))
            );
            return component;
        }
        return component;
    }
}