package de.keeeks.nucleo.modules.common.commands.velocity;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.common.commands.api.translation.CommonCommandsTranslationRegistry;
import de.keeeks.nucleo.modules.common.commands.velocity.cloudnet.CloudNetServiceEventListener;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.ModulesCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.UptimeCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.economy.CookiesCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.players.PingCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.players.PlaytimeCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.ping.PlayerPingRequestPacketListener;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.teamjoin.StaffMemberNetworkDisconnectPacketListener;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.teamjoin.StaffMemberNetworkJoinPacketListener;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.inject.InjectionLayer;

@ModuleDescription(
        name = "common-commands",
        depends = {"translations"},
        softDepends = {"players", "messaging", "lejet", "verifica", "config", "notifications"}
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
        boolean economyModuleEnabled = Module.isAvailable("economy");
        boolean notificationsModuleEnabled = Module.isAvailable("notifications");

        if (playersModuleEnabled) {
            registerCommands(new PingCommand());
            if (lejetModuleEnabled) {
                registerCommands(new PlaytimeCommand());
            }
        }

        if (messagingModuleEnabled) {
            NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);
            natsConnection.registerPacketListener(new PlayerPingRequestPacketListener(this));

            if (notificationsModuleEnabled && playersModuleEnabled && lejetModuleEnabled) {
                natsConnection.registerPacketListener(
                        new StaffMemberNetworkJoinPacketListener(proxyServer),
                        new StaffMemberNetworkDisconnectPacketListener(proxyServer)
                );
            }
        }

        if (economyModuleEnabled) {
            registerCommands(new CookiesCommand());
        }

        InjectionLayer.ext().instance(EventManager.class).registerListener(
                new CloudNetServiceEventListener(proxyServer)
        );
    }
}