package de.keeeks.nucleo.core.application.console.completer;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.application.console.command.Command;
import de.keeeks.nucleo.core.application.console.command.CommandRegistry;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class CommandsCompleter implements Completer {

    private final Supplier<List<Command>> commands;

    public CommandsCompleter() {
        commands = () -> ServiceRegistry.service(CommandRegistry.class).commands();
    }

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        if (parsedLine.words().isEmpty()) {
            commands.get().forEach(command -> addCommandToCandidates(command, list));
            return;
        }

        var commandName = parsedLine.words().get(0);

        var availableCommands = commands.get().stream().filter(
                command -> isCommandForName(command, commandName)
        ).toList();
        if (availableCommands.isEmpty()) {
            commands.get().forEach(command -> addCommandToCandidates(command, list));
            return;
        }

        var args = parsedLine.words().toArray(new String[0]);

        availableCommands.forEach(command -> command.tabCompletion(
                parsedLine.wordIndex(),
                parsedLine.word(),
                args
        ).stream().map(
                Candidate::new
        ).forEach(list::add));
    }

    private void addCommandToCandidates(Command command, List<Candidate> candidates) {
        candidates.add(new Candidate(command.name()));
        for (String alias : command.aliases()) {
            candidates.add(new Candidate(alias));
        }
    }

    private boolean isCommandForName(Command command, String commandName) {
        if (commandName.isEmpty()) return false;
        return command.name().matches(
                "^" + commandName + "$"
        ) || Arrays.stream(command.aliases()).anyMatch(s -> s.matches("^" + commandName + "$"));
    }
}