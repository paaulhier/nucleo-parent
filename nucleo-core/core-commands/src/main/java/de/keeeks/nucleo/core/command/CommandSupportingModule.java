package de.keeeks.nucleo.core.command;

import de.keeeks.nucleo.core.api.ConditionTester;
import de.keeeks.nucleo.core.api.Module;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.autocomplete.AutoCompleter;
import revxrsal.commands.autocomplete.SuggestionProvider;

@RequiredArgsConstructor
public abstract class CommandSupportingModule extends Module {
    private final CommandHandler commandHandler;

    public final void registerCommands(Object... commands) {
        commandHandler.register(commands);
    }

    public final void registerAutoCompletion(String command, SuggestionProvider suggestionProvider) {
        commandHandler.getAutoCompleter().registerSuggestion(
                command,
                suggestionProvider
        );
    }

    public final AutoCompleter autoCompleter() {
        return commandHandler.getAutoCompleter();
    }

    public final <T extends CommandHandler> T commandHandler() {
        return (T) commandHandler;
    }

    public final void registerConditionally(ConditionTester conditionTester, Object... commands) {
        if (conditionTester.test()) {
            registerCommands(commands);
        }
    }
}