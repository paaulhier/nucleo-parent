package de.keeeks.nucleo.core.application.command.config;

public record ConsoleConfiguration(
        String prompt,
        String terminalName,
        String appName,
        String logFormat
) {

    public static ConsoleConfiguration createDefault() {
        return new ConsoleConfiguration(
                "%user% >",
                "Terminal",
                "Application",
                "logFormat"
        );
    }
}