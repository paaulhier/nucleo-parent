package de.keeeks.nucleo.core.application.command.logger.format;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.core.api.utils.StringUtils;
import de.keeeks.nucleo.core.application.command.Console;
import de.keeeks.nucleo.core.application.command.color.ConsoleColorReplacer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LogFormatter extends SimpleFormatter {
    private final String logFormat = ServiceRegistry.service(
            Console.class
    ).consoleConfiguration().logFormat();

    private final boolean fileFormat;

    @Override
    public String format(LogRecord record) {
        var text = recordMessage(record);
        return fileFormat
                ? ConsoleColorReplacer.replaceCommonColorCodesForFileFormat(text)
                : ConsoleColorReplacer.replaceCommonColors(text);
    }

    private String recordMessage(LogRecord record) {
        var stringBuilder = new StringBuilder(
                formatString(record, record.getMessage()) + (fileFormat ? "\n" : "")
        );

        var throwable = record.getThrown();
        if (throwable != null) {
            stringBuilder.append("\n");
            var stackTraceElements = throwable.getStackTrace();
            stringBuilder.append(throwable).append("\n");
            for (int i = 0; i < stackTraceElements.length; i++) {
                stringBuilder.append(
                        stackTraceElements[i]
                );

                if (i + 1 < stackTraceElements.length)
                    stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }

        return stringBuilder.toString();
    }

    private String formatString(LogRecord record, String text) {
        var threadName = threadById(record.getLongThreadID());
        var recordLevel = record.getLevel();
        return StringUtils.replace(
                logFormat,
                "%user%",
                System.getProperty("user.name", "root"),
                "%time%",
                Formatter.formatInstant(record.getInstant()),
                "%level%",
                formattedLogLevel(recordLevel),
                "%thread%",
                fileFormat ? threadName : stringWithSpecificLength(threadName, 20),
                "%message%",
                text
        );
    }

    private String formattedLogLevel(Level recordLevel) {
        var coloredLevelName = logLevelColor(recordLevel) + recordLevel.getLocalizedName();
        return fileFormat ? coloredLevelName.concat("&r") : stringWithSpecificLength(
                coloredLevelName,
                9
        ).concat("&r");
    }

    private String stringWithSpecificLength(String s, int length) {
        if (s.length() < length) {
            return s + " ".repeat(length - s.length());
        }
        if (s.length() > length) {
            return s.substring(0, (length - 3)) + ".".repeat(3);
        }
        return s;
    }

    private String logLevelColor(Level level) {
        if (level == Level.INFO) {
            return "&9";
        }
        if (level == Level.SEVERE) {
            return "&4";
        }
        if (level == Level.FINE || level == Level.FINER || level == Level.FINEST) {
            return "&a";
        }
        if (level == Level.WARNING) {
            return "&c";
        }
        if (level == Level.CONFIG) {
            return "&e";
        }
        if (level == Level.ALL) {
            return "&f";
        }
        return "&0";
    }

    private String threadById(long id) {
        return Thread.getAllStackTraces().keySet().stream().filter(
                thread -> thread.threadId() == id
        ).map(Thread::getName).findFirst().orElse(Thread.currentThread().getName());
    }

    public static LogFormatter create(boolean fileFormat) {
        return new LogFormatter(fileFormat);
    }
}