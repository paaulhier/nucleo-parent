package de.keeeks.nucleo.modules.common.commands.proxy.commands.team;

import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PrefixType;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.common.commands.proxy.CommonCommandsProxyModule;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.verifica.api.VerificaApi;
import de.keeeks.verifica.api.VerificationState;
import de.keeeks.verifica.api.platform.Platform;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bungee.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;

@Command({"pi", "playerinfo", "π", "3,14"})
@CommandPermission("nucleo.commands.playerinfo")
@RequiredArgsConstructor
public final class PlayerInfoCommand {
    private final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );
    private final PermissionApi permissionApi = PermissionApi.instance();
    private final VerificaApi verificaApi = ServiceRegistry.service(
            VerificaApi.class
    );

    @AutoComplete("@players")
    @DefaultFor({"pi", "playerinfo", "π", "3.14", "3,14"})
    public void playerInfo(
            Audience audience,
            @Optional String targetName
    ) {
        if (targetName == null) {
            audience.sendMessage(Component.translatable("commands.playerinfo.usage"));
            return;
        }

        var player = ProxyServer.getInstance().getPlayer(
                audience.get(Identity.UUID).orElseThrow()
        );

        playerService.player(targetName).ifPresentOrElse(
                nucleoPlayer -> {
                    Component groupsAsList = Component.join(
                            JoinConfiguration.commas(true),
                            permissionApi.user(nucleoPlayer.uuid()).stream().flatMap(
                                    permissionUser -> permissionUser.groups().stream()
                            ).map(
                                    permissionGroup -> permissionGroup.prefix(PrefixType.DISPLAY)
                            ).toList()
                    );

                    List<Component> arguments = new ArrayList<>(List.of(
                            Component.text(nucleoPlayer.name()),
                            Component.text(nucleoPlayer.uuid().toString()),
                            Component.text(Formatter.formatDateTime(
                                    nucleoPlayer.createdAt()
                            )),
                            nucleoPlayer.lastLogin() == null
                                    ? Component.translatable("commands.playerinfo.neverConnected")
                                    : Component.text(Formatter.formatDateTime(nucleoPlayer.lastLogin())
                            ),
                            nucleoPlayer.lastLogout() == null
                                    ? Component.translatable("commands.playerinfo.neverConnected")
                                    : Component.text(Formatter.formatDateTime(nucleoPlayer.lastLogout())
                            ),
                            Component.text(Formatter.formatDateTime(
                                    nucleoPlayer.updatedAt()
                            )),
                            Component.text(Formatter.formatLongTime(
                                    nucleoPlayer.onlineTime()
                            )),
                            groupsAsList,
                            verificationComponent(
                                    nucleoPlayer,
                                    Platform.DISCORD
                            ),
                            verificationComponent(
                                    nucleoPlayer,
                                    Platform.TEAMSPEAK
                            )
                    ));

                    if (nucleoPlayer instanceof NucleoOnlinePlayer nucleoOnlinePlayer) {
                        arguments.addAll(List.of(
                                Component.text(blurIpAddressIfNecessary(
                                        player,
                                        nucleoOnlinePlayer.ipAddress()
                                )),
                                Component.text(nucleoOnlinePlayer.proxy()),
                                Component.text(nucleoOnlinePlayer.server()),
                                Component.text(nucleoOnlinePlayer.version().version())
                        ));
                        Module.module(CommonCommandsProxyModule.class).logger().info(
                                "Proxy: " + nucleoOnlinePlayer.proxy() + " - Server: " + nucleoOnlinePlayer.server()
                        );
                        audience.sendMessage(Component.translatable(
                                "commands.playerinfo.playerInfo"
                        ).arguments(arguments));
                    } else {
                        audience.sendMessage(Component.translatable(
                                "commands.playerinfo.playerInfoOffline"
                        ).arguments(arguments));
                    }
                },
                () -> audience.sendMessage(Component.translatable(
                        "playerNotFound",
                        Component.text(targetName)
                ))
        );
    }

    @NotNull
    private Component verificationComponent(NucleoPlayer nucleoPlayer, Platform platform) {
        return verificaApi.verification(
                nucleoPlayer.uuid(),
                platform
        ).map(verification -> {
            VerificationState verificationState = verification.verificationState();

            if (verificationState == VerificationState.PENDING) {
                return Component.translatable("commands.playerinfo.pendingVerification");
            }
            return (Component) Component.text(verification.userId());
        }).orElse(Component.translatable(
                "commands.playerinfo.noVerification"
        ));
    }

    private String blurIpAddressIfNecessary(
            ProxiedPlayer player,
            String ipAddress
    ) {
        if (player.hasPermission("nucleo.commands.playerinfo.ip")) {
            return ipAddress;
        }
        return blurredIpAddress(ipAddress);
    }

    private String blurredIpAddress(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        return parts[0] + "." + parts[1] + ".xxx.xxx";
    }
}