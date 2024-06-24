package de.keeeks.nucleo.modules.automessage.velocity;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.velocity.commands.AutoMessageCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ModuleDescription(
        name = "automessage",
        dependencies = {
                @Dependency(name = "messaging"),
                @Dependency(name = "database-mysql"),
                @Dependency(name = "config"),
                @Dependency(name = "translations"),
                @Dependency(name = "players")
        }
)
public class AutoMessageVelocityModule extends VelocityModule {
    private AutomaticMessageApi messageApi;

    @Override
    public void load() {
        messageApi = ServiceRegistry.registerService(
                AutomaticMessageApi.class,
                new VelocityAutomaticMessageApi(this)
        );
    }

    @Override
    public void enable() {
        autoCompleter().registerSuggestion(
                "autoMessageIds",
                (list, commandActor, executableCommand) -> messageIdsAsList()
        );

        registerCommands(new AutoMessageCommand());
    }

    private @NotNull List<String> messageIdsAsList() {
        return messageApi.messages().stream().map(
                AutomaticMessage::id
        ).map(
                integer -> Integer.toString(integer)
        ).toList();
    }
}