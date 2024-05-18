package de.keeeks.nucleo.modules.buildserverconnector;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.buildserverconnector.commands.BuildServerCommand;
import de.keeeks.nucleo.modules.buildserverconnector.config.BuildServerConnectorConfiguration;
import de.keeeks.nucleo.modules.buildserverconnector.listener.ForcedHostJoinListener;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;

@ModuleDescription(
        name = "buildserverconnector",
        description = "This module connects a player when connecting to a forced host.",
        dependencies = {@Dependency(name = "config")}
)
public class BuildServerConnectorModule extends VelocityModule {
    @Override
    public void enable() {
        BuildServerConnectorConfiguration buildServerConnectorConfiguration = JsonConfiguration.create(dataFolder(), "config").loadObject(
                BuildServerConnectorConfiguration.class,
                BuildServerConnectorConfiguration.createDefault()
        );
        if (!buildServerConnectorConfiguration.enabled()) return;
        registerListener(new ForcedHostJoinListener(buildServerConnectorConfiguration, proxyServer));
        registerCommands(new BuildServerCommand(buildServerConnectorConfiguration, proxyServer));
    }
}