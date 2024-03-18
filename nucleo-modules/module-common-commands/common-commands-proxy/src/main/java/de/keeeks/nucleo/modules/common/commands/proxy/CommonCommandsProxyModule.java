package de.keeeks.nucleo.modules.common.commands.proxy;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.common.commands.api.translation.CommonCommandsTranslationRegistry;
import de.keeeks.nucleo.modules.common.commands.proxy.commands.ModulesCommand;
import de.keeeks.nucleo.modules.common.commands.proxy.commands.UptimeCommand;
import de.keeeks.nucleo.modules.common.commands.proxy.commands.players.PingCommand;
import de.keeeks.nucleo.modules.common.commands.proxy.commands.players.PlayerInfoCommand;
import de.keeeks.nucleo.modules.common.commands.proxy.commands.team.JumpToCommand;
import de.keeeks.nucleo.modules.common.commands.proxy.commands.team.TeamCommand;
import de.keeeks.nucleo.modules.common.commands.proxy.packet.listener.ping.PlayerPingRequestPacketListener;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "common-commands",
        depends = {"translations"},
        softDepends = {"players", "messaging", "lejet", "verifica"}
)
public class CommonCommandsProxyModule extends ProxyModule {

    @Override
    public void load() {
        TranslationRegistry.initializeRegistry(new CommonCommandsTranslationRegistry(
                this
        ));
    }

    @Override
    public void enable() {
        registerCommands(
                new ModulesCommand(plugin()),
                new UptimeCommand(plugin())
        );
        boolean playersModuleEnabled = Module.isAvailable("players");
        boolean messagingModuleEnabled = Module.isAvailable("messaging");
        boolean lejetModuleEnabled = Module.isAvailable("lejet");
        boolean verificaModuleEnabled = Module.isAvailable("verifica");

        if (playersModuleEnabled) {
            registerCommands(
                    new PingCommand(audiences()),
                    new JumpToCommand(audiences())
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
                    new PlayerPingRequestPacketListener()
            );
        }
    }
}