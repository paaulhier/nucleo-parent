package de.keeeks.nucleo.modules.teamchat;

import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionUser;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.teamchat.listener.TeamChatChatListener;
import de.keeeks.nucleo.modules.teamchat.packet.listener.TeamChatMessagePacketListener;
import de.keeeks.nucleo.modules.teamchat.translation.TeamChatTranslationRegistry;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@ModuleDescription(
        name = "teamchat",
        description = "This module provides a team chat.",
        depends = {"messaging", "notifications", "players", "lejet"}
)
public class TeamChatModule extends ProxyModule {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private NatsConnection natsConnection;

    @Override
    public void load() {
        this.natsConnection = ServiceRegistry.service(NatsConnection.class);
        PlayerService playerService = ServiceRegistry.service(PlayerService.class);
        PermissionApi permissionApi = PermissionApi.instance();

        natsConnection.registerPacketListener(new TeamChatMessagePacketListener(audiences(), uuid -> {
            String playerName = playerService.player(uuid).map(NucleoPlayer::name).orElse("Unknown");
            return permissionApi.user(uuid).flatMap(
                    PermissionUser::highestPermssionGroup
            ).map(permissionGroup -> miniMessage.deserialize(permissionGroup.color()).append(
                    Component.text(playerName)
            )).orElse(Component.text(playerName));
        }));
        TranslationRegistry.initializeRegistry(new TeamChatTranslationRegistry(this));
    }

    @Override
    public void enable() {
        registerListener(new TeamChatChatListener(natsConnection));
    }
}