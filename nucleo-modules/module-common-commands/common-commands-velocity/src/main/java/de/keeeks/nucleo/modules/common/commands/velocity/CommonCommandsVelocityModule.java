package de.keeeks.nucleo.modules.common.commands.velocity;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.common.commands.api.translation.CommonCommandsTranslationRegistry;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.ModulesCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.UptimeCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.economy.CookiesCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.players.PingCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.team.JumpToCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.team.PlayerInfoCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.team.TeamCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.team.administration.PushCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.configuration.PushConfiguration;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.ping.PlayerPingRequestPacketListener;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "common-commands",
        depends = {"translations"},
        softDepends = {"players", "messaging", "lejet", "verifica", "config"}
)
public class CommonCommandsVelocityModule extends VelocityModule {

    @Override
    public void load() {
        TranslationRegistry.initializeRegistry(new CommonCommandsTranslationRegistry(
                this
        ));
    }

    @Override
    public void enable() {
        registerCommands(
                new ModulesCommand(),
                new UptimeCommand(plugin)
        );
        boolean playersModuleEnabled = Module.isAvailable("players");
        boolean messagingModuleEnabled = Module.isAvailable("messaging");
        boolean lejetModuleEnabled = Module.isAvailable("lejet");
        boolean verificaModuleEnabled = Module.isAvailable("verifica");
        boolean configModuleEnabled = Module.isAvailable("config");
        boolean economyModuleEnabled = Module.isAvailable("economy");

        if (configModuleEnabled) {
            registerCommands(new PushCommand(pushConfiguration()));
        }

        if (playersModuleEnabled) {
            registerCommands(
                    new PingCommand(),
                    new JumpToCommand()
            );
            if (lejetModuleEnabled && verificaModuleEnabled) {
                registerCommands(new PlayerInfoCommand());
            }
            if (lejetModuleEnabled) {
                registerCommands(new TeamCommand());
            }
        }

        if (messagingModuleEnabled) {
            NatsConnection natsConnection = ServiceRegistry.service(
                    NatsConnection.class
            );
            natsConnection.registerPacketListener(
                    new PlayerPingRequestPacketListener(this)
            );
        }

        if (economyModuleEnabled) {
            registerCommands(new CookiesCommand());
        }
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