package de.keeeks.nucleo.core.application.command.task;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.application.command.Console;
import de.keeeks.nucleo.core.application.command.command.Command;
import de.keeeks.nucleo.core.application.command.command.CommandRegistry;
import de.keeeks.nucleo.core.application.command.logger.ConsoleLogger;

import java.util.Arrays;
import java.util.logging.Logger;

public class LineReadingThread extends Thread {

    private final Console console;
    private ConsoleLogger logger;

    private LineReadingThread(Console console) {
        super("Command reader");
        this.console = console;

        Runtime.getRuntime().addShutdownHook(new Thread(this::interrupt));
    }

    @Override
    public void run() {
        String line;

        while (!interrupted()) {
            line = console.readLineOrNull();
            if (line == null) continue;

            var args = line.split(" ");
            var commandName = args[0];

            ServiceRegistry.service(
                    CommandRegistry.class
            ).command(commandName).ifPresentOrElse(
                    command -> executeCommandAsync(command, Arrays.copyOfRange(args, 1, args.length)),
                    () -> logger().info("Unknown command. Please type \"help\" for help.")
            );
        }
    }

    private void executeCommandAsync(Command command, String[] args) {
        Scheduler.runAsync(() -> command.execute(args));
    }

    private Logger logger() {
        if (logger == null) {
            logger = ConsoleLogger.create(LineReadingThread.class);
        }
        return logger;
    }

    public static LineReadingThread create(Console console) {
        return new LineReadingThread(console);
    }
}