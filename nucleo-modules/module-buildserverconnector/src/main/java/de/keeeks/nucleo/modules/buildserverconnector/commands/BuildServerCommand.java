package de.keeeks.nucleo.modules.buildserverconnector.commands;

import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.modules.buildserverconnector.config.BuildServerConnectorConfiguration;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.velocity.annotation.CommandPermission;

@Command({"bauserver", "buildserver"})
@CommandPermission("nucleo.commands.buildserver")
@RequiredArgsConstructor
public class BuildServerCommand {
    private final BuildServerConnectorConfiguration configuration;
    private final ProxyServer proxyServer;

    @DefaultFor("~")
    public void buildServerCommand(Player player) {
        if (!player.hasPermission(configuration.requiredPermission())) return;

        proxyServer.getServer(configuration.serverName()).map(
                player::createConnectionRequest
        ).ifPresent(ConnectionRequestBuilder::connect);
    }

}