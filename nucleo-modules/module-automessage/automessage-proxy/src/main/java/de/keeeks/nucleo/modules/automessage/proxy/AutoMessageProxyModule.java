package de.keeeks.nucleo.modules.automessage.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.proxy.commands.AutoMessageCommand;
import de.keeeks.nucleo.modules.automessage.shared.translation.AutoMessageTranslationRegistry;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "automessage",
        depends = {"messaging", "database-mysql", "config", "translations", "players"}
)
public class AutoMessageProxyModule extends ProxyModule {
    private AutomaticMessageApi messageApi;

    @Override
    public void load() {
        messageApi = ServiceRegistry.registerService(
                AutomaticMessageApi.class,
                new ProxyAutomaticMessageApi(this)
        );
    }

    @Override
    public void enable() {
        TranslationRegistry.initializeRegistry(new AutoMessageTranslationRegistry(this));

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