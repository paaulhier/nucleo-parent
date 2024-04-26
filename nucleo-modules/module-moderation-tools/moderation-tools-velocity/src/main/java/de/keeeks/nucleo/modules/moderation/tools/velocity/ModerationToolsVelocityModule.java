package de.keeeks.nucleo.modules.moderation.tools.velocity;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.NucleoClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.translation.ModerationToolsTranslationRegistry;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.ClicksPerSecondCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.TeamCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.administration.PushCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player.AltsCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player.PlayerInfoCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.configuration.PushConfiguration;
import de.keeeks.nucleo.modules.moderation.tools.velocity.listener.ModerationToolsPlayerDisconnectListener;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "moderation-tools",
        description = "A module for moderation tools like e.g. click checks",
        depends = {"players", "messaging"}
)
public class ModerationToolsVelocityModule extends VelocityModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                ClickCheckApi.class,
                new NucleoClickCheckApi(ServiceRegistry.service(NatsConnection.class))
        );
        TranslationRegistry.initializeRegistry(new ModerationToolsTranslationRegistry(this));
    }

    @Override
    public void enable() {
        registerCommands(
                new ClicksPerSecondCommand(),
                new AltsCommand()
        );

        boolean configModuleEnabled = Module.isAvailable("config");
        boolean playersModuleEnabled = Module.isAvailable("players");
        boolean lejetModuleEnabled = Module.isAvailable("lejet");
        boolean verificaModuleEnabled = Module.isAvailable("verifica");

        registerConditionally(() -> configModuleEnabled, new PushCommand(pushConfiguration()));
        registerConditionally(
                () -> playersModuleEnabled && lejetModuleEnabled && verificaModuleEnabled,
                new PlayerInfoCommand()
        );
        registerConditionally(
                () -> lejetModuleEnabled,
                new TeamCommand()
        );

        registerListener(new ModerationToolsPlayerDisconnectListener());
    }

    private PushConfiguration pushConfiguration() {
        return JsonConfiguration.create(
                dataFolder(),
                "push"
        ).loadObject(
                PushConfiguration.class,
                PushConfiguration.createDefault()
        );
    }
}