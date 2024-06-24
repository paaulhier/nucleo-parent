package de.keeeks.nucleo.core.command;

import de.keeeks.nucleo.core.api.ConditionTester;
import de.keeeks.nucleo.core.api.Module;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.CommandHandlerVisitor;
import revxrsal.commands.autocomplete.AutoCompleter;
import revxrsal.commands.autocomplete.SuggestionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class CommandSupportingModule extends Module {
    private final Supplier<CommandHandler> commandHandler;

    public final void registerCommands(Object... commands) {
        if (commandHandler.get() == null) {
            throw new IllegalStateException("CommandHandler is not initialized yet. " +
                    "Use command registration in enable method.");
        }
        for (Object command : commands) {
            commandHandler.get().register(command);
            if (command instanceof CommandHandlerVisitor commandHandlerVisitor) {
                commandHandlerVisitor.visit(commandHandler.get());
            }
        }
    }

    public final void registerAutoCompletion(String command, SuggestionProvider suggestionProvider) {
        commandHandler.get().getAutoCompleter().registerSuggestion(
                command,
                suggestionProvider
        );
    }

    public final AutoCompleter autoCompleter() {
        return commandHandler.get().getAutoCompleter();
    }

    public final <T extends CommandHandler> T commandHandler() {
        return (T) commandHandler.get();
    }

    public final void registerConditionally(ConditionTester conditionTester, Object... commands) {
        if (conditionTester.test()) {
            registerCommands(commands);
        }
    }
}