package de.keeeks.nucleo.modules.players.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.GameProfile;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

import static net.kyori.adventure.text.Component.text;

public class LoginListener {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    @Subscribe(order = PostOrder.FIRST)
    public EventTask handleVersionCheck(LoginEvent event) {
        Player player = event.getPlayer();

        return EventTask.async(() -> {
            int protocol = player.getProtocolVersion().getProtocol();
            Version version = Version.byProtocol(protocol);

            if (version == null) {
                event.setResult(ResultedEvent.ComponentResult.denied(versionNotSupportedText()));
            }

            if (playerService.player(player.getUniqueId()).isEmpty()) {
                NucleoPlayer nucleoPlayer = playerService.createPlayer(
                        player.getUniqueId(),
                        player.getUsername()
                ).updateLastLogin().updateLastIpAddress(
                        player.getRemoteAddress().getHostString()
                );
                handleSkinUpdate(
                        nucleoPlayer,
                        player.getGameProfileProperties().getFirst()
                );
                playerService.savePlayerToDatabase(nucleoPlayer);
                nucleoPlayer.update();
            } else {
                NucleoPlayer nucleoPlayer = playerService.player(player.getUniqueId()).orElseThrow();
                nucleoPlayer.updateLastLogin().updateLastIpAddress(
                        player.getRemoteAddress().getHostString()
                );
                if (!nucleoPlayer.name().equals(player.getUsername())) {
                    nucleoPlayer.updateName(player.getUsername());
                }
                playerService.savePlayerToDatabase(nucleoPlayer);
                nucleoPlayer.update();
            }
        });
    }

    private Component versionNotSupportedText() {
        return text("Your client version is not supported by this server. Please use %s".formatted(
                Version.supportedVersionsAsString()
        ), Style.style(NamedTextColor.RED));
    }

    @Subscribe(order = PostOrder.NORMAL)
    public EventTask handlePlayerCreation(LoginEvent event) {
        Player player = event.getPlayer();
        InetSocketAddress address = player.getRemoteAddress();
        GameProfile.Property property = player.getGameProfileProperties().getFirst();
        Version version = Version.byProtocol(player.getProtocolVersion().getProtocol());

        return EventTask.async(() -> {
            if (!event.getResult().isAllowed()) return;

            String ipAddress = address.getHostString();
            playerService.onlinePlayer(player.getUniqueId()).ifPresentOrElse(
                    onlinePlayer -> event.setResult(alreadyOnlineDisconnect()),
                    () -> playerService.player(player.getUniqueId()).ifPresentOrElse(nucleoPlayer -> handlePlayerJoin(
                                    nucleoPlayer,
                                    property,
                                    player,
                                    ipAddress,
                                    version
                            ), () -> handlePlayerFirstJoin(
                                    player,
                                    property,
                                    ipAddress,
                                    version
                            )
                    )
            );
        });
    }

    private static ResultedEvent.@NotNull ComponentResult alreadyOnlineDisconnect() {
        return ResultedEvent.ComponentResult.denied(
                text("You are already connected to the server.", Style.style(NamedTextColor.RED))
        );
    }

    private void handlePlayerFirstJoin(Player player, GameProfile.Property property, String ipAddress, Version version) {
        NucleoPlayer nucleoPlayer = playerService.createPlayer(
                player.getUniqueId(),
                player.getUsername()
        );
        handlePlayerJoin(
                nucleoPlayer,
                property,
                player,
                ipAddress,
                version
        );
    }

    private void handlePlayerJoin(
            NucleoPlayer nucleoPlayer,
            GameProfile.Property property,
            Player player,
            String ipAddress,
            Version version
    ) {
        handleSkinUpdate(
                nucleoPlayer,
                property
        );
        if (!nucleoPlayer.name().equals(player.getUsername())) {
            playerService.updatePlayerName(
                    nucleoPlayer.uuid(),
                    player.getUsername()
            );
        }
        handleOnlinePlayerCreation(nucleoPlayer, ipAddress, version);
    }

    private void handleSkinUpdate(NucleoPlayer onlinePlayer, GameProfile.Property property) {
        if (property.getName().equals("textures")) {
            onlinePlayer.updateSkin(
                    property.getValue(),
                    property.getSignature()
            );
        }
    }

    private void handleOnlinePlayerCreation(
            NucleoPlayer nucleoPlayer,
            String address,
            Version version
    ) {
        NucleoOnlinePlayer onlinePlayer = playerService.createOnlinePlayer(
                nucleoPlayer,
                "",
                Module.serviceName(),
                address,
                version
        );
        onlinePlayer.updateLastLogin();
        playerService.savePlayerToDatabase(onlinePlayer);
        playerService.publishOnlinePlayerCreation(onlinePlayer);
    }
}