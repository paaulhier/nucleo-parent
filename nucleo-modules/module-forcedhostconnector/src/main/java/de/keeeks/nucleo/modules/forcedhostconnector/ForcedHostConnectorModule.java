package de.keeeks.nucleo.modules.forcedhostconnector;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.forcedhostconnector.config.ForcedHost;
import de.keeeks.nucleo.modules.forcedhostconnector.config.ForcedHostCommand;
import de.keeeks.nucleo.modules.forcedhostconnector.config.ForcedHostConfiguration;
import de.keeeks.nucleo.modules.forcedhostconnector.listener.ForcedHostJoinListener;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@ModuleDescription(
        name = "forcedhostconnector",
        description = "This module connects a player when connecting to a forced host.",
        dependencies = @Dependency(name = "config")
)
public class ForcedHostConnectorModule extends VelocityModule {
    @Override
    public void enable() {
        ForcedHostConfiguration forcedHostConfiguration = JsonConfiguration.create(dataFolder(), "config").loadObject(
                ForcedHostConfiguration.class,
                ForcedHostConfiguration.createDefault()
        );
        registerListener(new ForcedHostJoinListener(forcedHostConfiguration, proxyServer));

        for (ForcedHost forcedHost : forcedHostConfiguration.forcedHosts()) {
            if (!forcedHost.enabled()) continue;
            ForcedHostCommand forcedHostCommand = forcedHost.command();
            if (forcedHostCommand == null) continue;

            createAndRegisterCommand(forcedHost, forcedHostCommand);
        }
    }

    private void createAndRegisterCommand(
            ForcedHost forcedHost,
            ForcedHostCommand forcedHostCommand
    ) {
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(forcedHostCommand.command())
                .aliases(forcedHostCommand.aliases())
                .plugin(plugin)
                .build();

        commandManager.register(
                commandMeta,
                (SimpleCommand) invocation -> {
                    if (invocation.source() instanceof Player player) {
                        if (!forcedHostCommand.hasPermission(player)) return;
                        proxyServer.getServer(forcedHost.serverName()).map(
                                player::createConnectionRequest
                        ).ifPresent(ConnectionRequestBuilder::connect);
                    }
                }
        );
    }
}