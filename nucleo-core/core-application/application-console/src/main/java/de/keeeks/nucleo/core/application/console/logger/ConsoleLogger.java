package de.keeeks.nucleo.core.application.console.logger;

import de.keeeks.nucleo.core.application.console.logger.format.LogFormatter;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleLogger extends Logger {
    private static final FileHandler fileHandler;
    private static final ConsoleHandler consoleHandler;

    static {
        File logsFolder = new File("logs/");
        if (!logsFolder.exists()) logsFolder.mkdirs();
        try {
            fileHandler = new FileHandler(
                    "logs/%u.log",
                    1024 * 1024 * 10,
                    10,
                    true
            );
            fileHandler.setFormatter(LogFormatter.create(true));

            consoleHandler = new CustomConsoleHandler();
            consoleHandler.setFormatter(LogFormatter.create(false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConsoleLogger(String name) {
        super(name, null);
        setLevel(Level.ALL);

        try {
            addHandler(fileHandler);
            addHandler(consoleHandler);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static ConsoleLogger create(Class<?> clazz) {
        return create(clazz.getName());
    }

    public static ConsoleLogger create(String name) {
        return new ConsoleLogger(name);
    }
}