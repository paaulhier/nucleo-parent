package de.keeeks.nucleo.modules.syncproxy.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.syncproxy.api.SyncProxyService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.velocity.annotation.CommandPermission;

@Command({"syncproxy", "sp"})
@CommandPermission("nucleo.command.syncproxy")
@RequiredArgsConstructor
public class SyncProxyCommand {

    private final SyncProxyService syncProxyService = ServiceRegistry.service(
            SyncProxyService.class
    );


    @DefaultFor({"syncproxy", "sp"})
    public void syncProxyCommand(Player player) {
        player.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.usage"
        ));
    }

    @Subcommand("reload")
    public void reloadSubCommand(Player player) {
        player.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.reload"
        ));

        syncProxyService.reloadNetworkWide();
    }

    @Subcommand("list")
    public void listSubCommand(Player player) {
        player.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.list"
        ));

        syncProxyService.configurations().forEach(syncProxyConfiguration -> player.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.list-entry",
                Component.text(syncProxyConfiguration.name())
        )));
    }


    @AutoComplete("@syncproxy:configurations")
    @Subcommand({"activate", "enable"})
    public void activateConfiguration(
            final Player player,
            final String configurationName
    ) {
        syncProxyService.configuration(configurationName).ifPresentOrElse(
                syncProxyConfiguration -> {
                    syncProxyService.activateConfiguration(syncProxyConfiguration.id());
                    player.sendMessage(Component.translatable(
                            "nucleo.command.syncproxy.configuration-activated"
                    ));
                },
                () -> player.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.configuration-not-found"
                ))
        );
    }

    @AutoComplete("@syncproxy:configurations")
    @Subcommand("info")
    public void infoSubCommand(
            final Player player,
            final String configurationName
    ) {
        syncProxyService.configuration(configurationName).ifPresentOrElse(
                syncProxyConfiguration -> player.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.info",
                        Component.text(syncProxyConfiguration.name()),
                        Component.text(syncProxyConfiguration.maintenance()),
                        Component.text(syncProxyConfiguration.maxPlayers()),
                        Component.text(syncProxyConfiguration.motdConfigurations().size()),
                        Component.text(syncProxyConfiguration.active()),
                        Component.text(syncProxyConfiguration.id())
                )),
                () -> player.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.configuration-not-found"
                ))
        );
    }

    @AutoComplete("@syncproxy:configurations @booleans")
    @Subcommand("set maintenance")
    public void setMaintenanceSubCommand(
            final Player player,
            final String configurationName,
            final boolean maintenance
    ) {
        syncProxyService.configuration(configurationName).ifPresentOrElse(
                syncProxyConfiguration -> {
                    syncProxyService.updateConfigurationNetworkWide(
                            syncProxyConfiguration.maintenance(maintenance)
                    );
                    player.sendMessage(Component.translatable(
                            "nucleo.command.syncproxy.maintenance-%s".formatted(
                                    maintenance ? "enabled" : "disabled"
                            )
                    ));
                },
                () -> player.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.configuration-not-found"
                ))
        );
    }

    @AutoComplete("@syncproxy:configurations")
    @Subcommand("set maxplayers")
    public void setMaxPlayersSubCommand(
            final Player player,
            final String configurationName,
            final int maxPlayers
    ) {
        syncProxyService.configuration(configurationName).ifPresentOrElse(
                syncProxyConfiguration -> {
                    syncProxyService.updateConfigurationNetworkWide(
                            syncProxyConfiguration.maxPlayers(maxPlayers)
                    );
                    player.sendMessage(Component.translatable(
                            "nucleo.command.syncproxy.maxplayers-set"
                    ));
                },
                () -> player.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.configuration-not-found"
                ))
        );
    }
}