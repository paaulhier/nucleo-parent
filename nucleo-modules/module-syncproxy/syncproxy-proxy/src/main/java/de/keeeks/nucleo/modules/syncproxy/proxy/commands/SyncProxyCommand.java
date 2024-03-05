package de.keeeks.nucleo.modules.syncproxy.proxy.commands;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;

@Command({"syncproxy", "sp"})
@CommandPermission("nucleo.command.syncproxy")
@RequiredArgsConstructor
public class SyncProxyCommand {

    private final SyncProxyService syncProxyService = ServiceRegistry.service(
            SyncProxyService.class
    );

    private final BungeeAudiences bungeeAudiences;

    @DefaultFor({"syncproxy", "sp"})
    public void syncProxyCommand(
            final BungeeCommandActor actor
    ) {
        if (!actor.isPlayer()) return;

        Audience audience = bungeeAudiences.player(actor.getUniqueId());
        audience.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.usage"
        ));
    }

    @Subcommand("reload")
    public void reloadSubCommand(
            final BungeeCommandActor actor
    ) {
        if (!actor.isPlayer()) return;

        Audience audience = bungeeAudiences.player(actor.getUniqueId());
        audience.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.reload"
        ));

        syncProxyService.reloadNetworkWide();
    }

    @Subcommand("list")
    public void listSubCommand(
            final BungeeCommandActor actor
    ) {
        if (!actor.isPlayer()) return;

        Audience audience = bungeeAudiences.player(actor.getUniqueId());
        audience.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.list"
        ));

        syncProxyService.configurations().forEach(syncProxyConfiguration -> audience.sendMessage(Component.translatable(
                "nucleo.command.syncproxy.list-entry",
                Component.text(syncProxyConfiguration.name())
        )));
    }

    @AutoComplete("@syncproxy:configurations")
    @Subcommand("info")
    public void infoSubCommand(
            final BungeeCommandActor actor,
            final String configurationName
    ) {
        if (!actor.isPlayer()) return;

        Audience audience = bungeeAudiences.player(actor.getUniqueId());

        syncProxyService.configuration(configurationName).ifPresentOrElse(
                syncProxyConfiguration -> audience.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.info",
                        Component.text(syncProxyConfiguration.name()),
                        Component.text(syncProxyConfiguration.maintenance()),
                        Component.text(syncProxyConfiguration.maxPlayers()),
                        Component.text(syncProxyConfiguration.motdConfigurations().size()),
                        Component.text(syncProxyConfiguration.active()),
                        Component.text(syncProxyConfiguration.id())
                )),
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.configuration-not-found"
                ))
        );
    }

    @AutoComplete("@syncproxy:configurations @booleans")
    @Subcommand("set maintenance")
    public void setMaintenanceSubCommand(
            final BungeeCommandActor actor,
            final String configurationName,
            final boolean maintenance
    ) {
        if (!actor.isPlayer()) return;

        Audience audience = bungeeAudiences.player(actor.getUniqueId());

        syncProxyService.configuration(configurationName).ifPresentOrElse(
                syncProxyConfiguration -> {
                    syncProxyService.updateConfigurationNetworkWide(
                            syncProxyConfiguration.maintenance(maintenance)
                    );
                    audience.sendMessage(Component.translatable(
                            "nucleo.command.syncproxy.maintenance-%s".formatted(
                                    maintenance ? "enabled" : "disabled"
                            )
                    ));
                },
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.configuration-not-found"
                ))
        );
    }

    @AutoComplete("@syncproxy:configurations")
    @Subcommand("set maxplayers")
    public void setMaxPlayersSubCommand(
            final BungeeCommandActor actor,
            final String configurationName,
            final int maxPlayers
    ) {
        if (!actor.isPlayer()) return;

        Audience audience = bungeeAudiences.player(actor.getUniqueId());

        syncProxyService.configuration(configurationName).ifPresentOrElse(
                syncProxyConfiguration -> {
                    syncProxyService.updateConfigurationNetworkWide(
                            syncProxyConfiguration.maxPlayers(maxPlayers)
                    );
                    audience.sendMessage(Component.translatable(
                            "nucleo.command.syncproxy.maxplayers-set"
                    ));
                },
                () -> audience.sendMessage(Component.translatable(
                        "nucleo.command.syncproxy.configuration-not-found"
                ))
        );
    }
}