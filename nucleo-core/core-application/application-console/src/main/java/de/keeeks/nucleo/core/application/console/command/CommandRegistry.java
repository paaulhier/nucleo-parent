package de.keeeks.nucleo.core.application.console.command;

import de.keeeks.nucleo.core.application.console.logger.ConsoleLogger;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public final class CommandRegistry {
    private static final ConsoleLogger logger = ConsoleLogger.create(CommandRegistry.class);

    private final List<Command> commands = new ArrayList<>();

    private final String basePackage;
    
    public void registerCommands() {
        Reflections reflections = new Reflections(basePackage);

        reflections.getSubTypesOf(Command.class).stream().map(
                this::createClassSafe
        ).forEach(this::registerCommands);
    }

    private Command createClassSafe(Class<? extends Command> aClass) {
        try {
            return aClass.getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Command> command(String name) {
        return commands.stream().filter(
                command -> command.matches(name)
        ).findFirst();
    }

    public void regísterCommand(Command command) {
        commands.add(command);
        logger.info("Registered command %s with %s".formatted(
                command.name(),
                command.hasAliases() ? "aliases %s".formatted(
                        String.join(", ", command.aliases())
                ) : "no aliases"
        ));
    }

    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            regísterCommand(command);
        }
    }

    public List<Command> commands() {
        return List.copyOf(commands);
    }
}