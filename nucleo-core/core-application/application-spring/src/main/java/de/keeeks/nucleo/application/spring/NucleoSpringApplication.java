package de.keeeks.nucleo.application.spring;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.application.command.Console;
import de.keeeks.nucleo.core.application.command.command.CommandRegistry;
import de.keeeks.nucleo.core.application.command.config.ConsoleConfiguration;
import de.keeeks.nucleo.core.application.command.logger.ConsoleLogger;
import de.keeeks.nucleo.core.loader.ModuleLoader;
import de.keeeks.nucleo.core.loader.classloader.ModuleClassLoader;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication(scanBasePackages = "de.keeeks")
public class NucleoSpringApplication {
    private static final Logger logger = Logger.getLogger(NucleoSpringApplication.class.getName());
    private static final ModuleLoader moduleLoader = ModuleLoader.create(logger);

    public static void main(String[] args) {
        ServiceRegistry.registerService(
                Console.class,
                Console.create(consoleConfiguration())
        );
        ConsoleLogger.parent(() -> logger);
        ModuleLoader.classLoader(ModuleClassLoader.create(
                Thread.currentThread().getContextClassLoader()
        ));
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.log(
                Level.SEVERE,
                "Uncaught exception in thread " + t.getName(),
                e
        ));
        new SpringApplicationBuilder()
                .main(NucleoSpringApplication.class)
                .registerShutdownHook(true)
                .sources(NucleoSpringApplication.class)
                .banner(new ResourceBanner(new FileSystemResource("banner.txt")))
                .build(args)
                .run(args);
    }

    @Bean
    public ModuleLoader moduleLoader() {
        return moduleLoader;
    }

    @Bean
    public CommandRegistry commandRegistry() {
        return ServiceRegistry.registerService(
                CommandRegistry.class,
                new CommandRegistry("de.keeeks")
        );
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