package de.keeeks.nucleo.core.application.command.command;

import de.keeeks.nucleo.core.application.command.logger.ConsoleLogger;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public abstract class Command {
    protected final ConsoleLogger logger;

    protected final String name;
    protected final String[] aliases;

    protected Command(String name) {
        this.logger = ConsoleLogger.create(Command.class);
        this.name = name;
        this.aliases = new String[0];
    }

    public Command(String name, String... aliases) {
        this.logger = ConsoleLogger.create(Command.class);
        this.name = name;
        this.aliases = aliases;
    }

    public Command(ConsoleLogger consoleLogger, String name, String[] aliases) {
        this.logger = consoleLogger;
        this.name = name;
        this.aliases = aliases;
    }

    public abstract void execute(String[] args);

    public List<String> tabCompletion(int index, String currentWord, String[] args) {
        return List.of();
    }

    public final boolean hasAliases() {
        return aliases.length > 0;
    }

    public final boolean matches(String matchingString) {
        return name.equalsIgnoreCase(matchingString) || Arrays.stream(aliases).anyMatch(
                alias -> alias.equalsIgnoreCase(matchingString)
        );
    }
}