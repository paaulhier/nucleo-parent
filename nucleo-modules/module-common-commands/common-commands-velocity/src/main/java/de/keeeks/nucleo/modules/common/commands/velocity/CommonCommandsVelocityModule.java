package de.keeeks.nucleo.modules.common.commands.velocity;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.common.commands.velocity.cloudnet.CloudNetServiceEventListener;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.HelpCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.ListCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.ModulesCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.UptimeCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.economy.CookiesCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.players.PingCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.players.PlaytimeCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.commands.players.TopCommand;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.ping.PlayerPingRequestPacketListener;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.teamjoin.StaffMemberNetworkDisconnectPacketListener;
import de.keeeks.nucleo.modules.common.commands.velocity.packet.listener.teamjoin.StaffMemberNetworkJoinPacketListener;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import revxrsal.commands.velocity.VelocityCommandActor;

import java.util.List;

@ModuleDescription(
        name = "common-commands",
        dependencies = {
                @Dependency(name = "translations"),
                @Dependency(name = "players", required = false),
                @Dependency(name = "messaging", required = false),
                @Dependency(name = "lejet", required = false),
                @Dependency(name = "notifications", required = false),
                @Dependency(name = "config", required = false)
        }
)
public class CommonCommandsVelocityModule extends VelocityModule {

    @Override
    public void enable() {
        registerCommands(
                new ModulesCommand(),
                new UptimeCommand(plugin),
                new HelpCommand()
        );
        boolean playersModuleEnabled = Module.isAvailable("players");
        boolean messagingModuleEnabled = Module.isAvailable("messaging");
        boolean lejetModuleEnabled = Module.isAvailable("lejet");
        boolean economyModuleEnabled = Module.isAvailable("economy");
        boolean notificationsModuleEnabled = Module.isAvailable("notifications");

        autoCompleter().registerSuggestion("servers", (list, commandActor, executableCommand) -> {
            if (commandActor.as(VelocityCommandActor.class).getSource().hasPermission("nucleo.command.list.server")) {
                return proxyServer.getAllServers().stream().map(
                        registeredServer -> registeredServer.getServerInfo().getName()
                ).toList();
            }
            return List.of();
        });

        registerConditionally(
                () -> playersModuleEnabled,
                new PingCommand(),
                new ListCommand(proxyServer)
        );
        registerConditionally(
                () -> playersModuleEnabled && lejetModuleEnabled,
                new PlaytimeCommand()
        );
        registerConditionally(
                () -> playersModuleEnabled && lejetModuleEnabled && economyModuleEnabled,
                new TopCommand()
        );
        registerConditionally(
                () -> economyModuleEnabled,
                new CookiesCommand()
        );


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

        InjectionLayer.ext().instance(EventManager.class).registerListener(
                new CloudNetServiceEventListener(proxyServer)
        );
    }
}