package de.keeeks.nucleo.core.application.console;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.application.command.Console;
import de.keeeks.nucleo.core.application.command.command.CommandRegistry;
import de.keeeks.nucleo.core.application.command.config.ConsoleConfiguration;
import de.keeeks.nucleo.core.application.command.logger.ConsoleLogger;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CoreConsoleApplication {
    private static final Console console = ServiceRegistry.registerService(
            Console.class,
            Console.create(consoleConfiguration())
    );
    @Getter
    private final Logger logger = ConsoleLogger.create(
            CoreConsoleApplication.class
    );
    private final ModuleLoader moduleLoader = ModuleLoader.create(logger);

    public CoreConsoleApplication() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.log(
                Level.SEVERE,
                "Uncaught exception in thread " + t.getName(),
                e
        ));
        ModuleLoader.classLoader(ModuleClassLoader.create(
                Thread.currentThread().getContextClassLoader()
        ));
        ServiceRegistry.registerService(
                CoreConsoleApplication.class,
                this
        );
    }

    public void load() {
        CommandRegistry commandRegistry = ServiceRegistry.registerService(
                CommandRegistry.class,
                new CommandRegistry("de.keeeks")
        );

        moduleLoader.loadModulesFromFolder();
        moduleLoader.loadModules();
        console.startLineReading();
        commandRegistry.registerCommands();
    }

    public void enable() {
        moduleLoader.enableModules();
    }

    public void disable() {
        Scheduler.shutdown();
        moduleLoader.disableModules();
    }

    private static ConsoleConfiguration consoleConfiguration() {
        Properties properties = new Properties();
        Path path = Paths.get("console.properties");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
                properties.setProperty("console.prompt", ">");
                properties.setProperty("console.terminalName", "Nucleo Console");
                properties.setProperty("console.applicationName", "Nucleo");
                properties.setProperty("console.logFormat", "format");
                properties.store(
                        Files.newBufferedWriter(path),
                        "Nucleo Console Configuration"
                );
            }

            properties.load(Files.newBufferedReader(
                    path
            ));

            return new ConsoleConfiguration(
                    properties.getProperty("console.prompt"),
                    properties.getProperty("console.terminalName"),
                    properties.getProperty("console.applicationName"),
                    properties.getProperty("console.logFormat")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}