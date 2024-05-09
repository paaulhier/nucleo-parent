package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.administration;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.PingOptions;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.time.Duration;

import static net.kyori.adventure.text.Component.*;

@Command("server")
@CommandPermission("nucleo.command.server")
@RequiredArgsConstructor
public class ServerCommand {
    private final ProxyServer proxyServer;

    @Usage("nucleo.command.server.usage")
    @AutoComplete("@servers")
    @DefaultFor("server")
    public void serverCommand(Player player, RegisteredServer registeredServer) {
        if (registeredServer == null) {
            player.sendMessage(translatable("nucleo.command.server.notFound"));
            return;
        }

        registeredServer.ping(PingOptions.builder()
                .version(player.getProtocolVersion())
                .timeout(Duration.ofSeconds(5))
                .build()).whenCompleteAsync((serverPing, pingingThrowable) -> {
            if (pingingThrowable != null || serverPing == null) {
                player.sendMessage(translatable("nucleo.command.server.failed.ping"));
                return;
            }

            player.createConnectionRequest(registeredServer).connect().whenCompleteAsync(
                    (result, connectingThrowable) -> {
                        if (connectingThrowable == null || !result.isSuccessful()) {
                            player.sendMessage(translatable(
                                    "nucleo.command.server.failed",
                                    result.getReasonComponent().orElse(empty()),
                                    text(result.getStatus().name())
                            ));
                            return;
                        }

                        player.sendMessage(translatable(
                                "nucleo.command.server.success",
                                text(registeredServer.getServerInfo().getName())
                        ));
                    }
            );
        });
    }
}