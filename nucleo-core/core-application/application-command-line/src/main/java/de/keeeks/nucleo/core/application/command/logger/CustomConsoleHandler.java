package de.keeeks.nucleo.core.application.command.logger;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.application.command.Console;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class CustomConsoleHandler extends ConsoleHandler {
    private final Console console = ServiceRegistry.service(
            Console.class
    );

    @Override
    public void publish(LogRecord record) {
        var format = getFormatter().format(record);
        console.lineReader().printAbove(format);
    }
}