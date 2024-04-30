package de.keeeks.nucleo.modules.automessage.velocity;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.velocity.commands.AutoMessageCommand;

@ModuleDescription(
        name = "automessage",
        depends = {"messaging", "database-mysql", "config", "translations", "players"}
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
                (list, commandActor, executableCommand) -> messageApi.messages().stream().map(
                        AutomaticMessage::id
                ).map(
                        integer -> Integer.toString(integer)
                ).toList()
        );

        registerCommands(new AutoMessageCommand());
    }
}