package de.keeeks.nucleo.modules.common.commands.velocity.commands.team;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionGroup;
import de.keeeks.lejet.api.permission.PrefixType;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.verifica.api.VerificaApi;
import de.keeeks.verifica.api.VerificationState;
import de.keeeks.verifica.api.platform.Platform;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static de.keeeks.lejet.api.NameColorizer.coloredName;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Command({"pi", "playerinfo", "π", "3,14"})
@CommandPermission("nucleo.commands.playerinfo")
@RequiredArgsConstructor
public final class PlayerInfoCommand {
    private final Economy economy = ServiceRegistry.service(EconomyApi.class).create("cookies");
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final VerificaApi verificaApi = ServiceRegistry.service(VerificaApi.class);
    private final PermissionApi permissionApi = PermissionApi.instance();

    @AutoComplete("@players")
    @DefaultFor({"pi", "playerinfo", "π", "3.14", "3,14"})
    public void playerInfo(
            Player player,
            @Optional String targetName
    ) {
        if (targetName == null) {
            player.sendMessage(translatable("commands.playerinfo.usage"));
            return;
        }

        playerService.player(targetName).ifPresentOrElse(
                nucleoPlayer -> {
                    Component groupsAsList = Component.join(
                            JoinConfiguration.commas(true),
                            permissionApi.user(nucleoPlayer.uuid()).stream().flatMap(
                                    permissionUser -> permissionUser.globalGroups().stream()
                            ).sorted(Comparator.comparingInt(PermissionGroup::priority)).map(
                                    permissionGroup -> permissionGroup.prefix(PrefixType.DISPLAY)
                            ).toList()
                    );

                    List<Component> arguments = new ArrayList<>(List.of(
                            coloredName(nucleoPlayer.uuid()).clickEvent(ClickEvent.copyToClipboard(
                                    nucleoPlayer.name()
                            )),
                            text(nucleoPlayer.uuid().toString()).clickEvent(ClickEvent.copyToClipboard(
                                    nucleoPlayer.uuid().toString()
                            )),
                            text(Formatter.formatDateTime(
                                    nucleoPlayer.createdAt()
                            )),
                            nucleoPlayer.lastLogin() == null
                                    ? translatable("commands.playerinfo.neverConnected")
                                    : text(Formatter.formatShortDateTime(nucleoPlayer.lastLogin())
                            ),
                            nucleoPlayer.lastLogout() == null
                                    ? translatable("commands.playerinfo.neverConnected")
                                    : text(Formatter.formatShortDateTime(nucleoPlayer.lastLogout())
                            ),
                            text(Formatter.formatShortDateTime(
                                    nucleoPlayer.updatedAt()
                            )),
                            text(Formatter.formatLongTime(
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
                            ),
                            text(economy.balance(player.getUniqueId()))
                    ));

                    if (nucleoPlayer instanceof NucleoOnlinePlayer nucleoOnlinePlayer) {
                        String ipAddress = blurIpAddressIfNecessary(
                                player,
                                nucleoOnlinePlayer.ipAddress()
                        );
                        arguments.addAll(List.of(
                                text(ipAddress).clickEvent(ClickEvent.copyToClipboard(ipAddress)),
                                text(nucleoOnlinePlayer.proxy()),
                                text(nucleoOnlinePlayer.server()).clickEvent(ClickEvent.runCommand(
                                        "/jumpto %s".formatted(nucleoOnlinePlayer.name())
                                )),
                                text(nucleoOnlinePlayer.version().version())
                        ));
                        player.sendMessage(translatable(
                                "commands.playerinfo.playerInfo"
                        ).arguments(arguments));
                    } else {
                        player.sendMessage(translatable(
                                "commands.playerinfo.playerInfoOffline"
                        ).arguments(arguments));
                    }
                },
                () -> player.sendMessage(translatable(
                        "playerNotFound",
                        text(targetName)
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
                return translatable("commands.playerinfo.pendingVerification");
            }
            return text(verification.userId()).clickEvent(ClickEvent.copyToClipboard(verification.userId()));
        }).orElse(translatable("commands.playerinfo.noVerification"));
    }

    private String blurIpAddressIfNecessary(
            Player player,
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