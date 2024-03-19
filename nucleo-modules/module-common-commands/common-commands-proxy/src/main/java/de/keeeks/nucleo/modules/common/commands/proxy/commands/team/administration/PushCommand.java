package de.keeeks.nucleo.modules.common.commands.proxy.commands.team.administration;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.discord.DiscordWebhook;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpirationListener;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpiringMap;
import de.keeeks.nucleo.modules.common.commands.proxy.CommonCommandsProxyModule;
import de.keeeks.nucleo.modules.common.commands.proxy.configuration.PushConfiguration;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bungee.annotation.CommandPermission;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Command("push")
@CommandPermission("nucleo.team.administration.push")
@RequiredArgsConstructor
public class PushCommand {
    private static final CommonCommandsProxyModule module = Module.module(CommonCommandsProxyModule.class);
    private final ExpiringMap<UUID, PushDetails> pushMap = ExpiringMap.builder()
            .expiration(30, TimeUnit.SECONDS)
            .asyncExpirationListener((ExpirationListener<UUID, PushDetails>) (key, value) -> {
                module.audiences().player(key).sendMessage(
                        Component.translatable("command.push.expired").arguments(
                                Component.text(value.message())
                        )
                );
            })
            .build();

    private final PushConfiguration pushConfiguration;

    @DefaultFor("push")
    public void pushCommand(
            Audience audience,
            @Optional String message
    ) {
        if (message == null) {
            audience.sendMessage(Component.translatable("command.push.usage"));
            return;
        }
        UUID uuid = audience.get(Identity.UUID).orElseThrow();

        if (pushMap.containsKey(uuid)) {
            audience.sendMessage(Component.translatable("command.push.alreadyPublishing"));
            return;
        }
        audience.sendMessage(Component.translatable("command.push.confirm"));
        pushMap.put(
                uuid,
                new PushDetails(uuid, message)
        );
    }

    @Subcommand("confirm")
    public void confirmCommand(
            Audience audience
    ) {
        UUID uuid = audience.get(Identity.UUID).orElseThrow();
        if (!pushMap.containsKey(uuid)) {
            audience.sendMessage(Component.translatable("command.push.noPending"));
            return;
        }

        audience.sendMessage(Component.translatable("command.push.publishing"));
        publishMessage(pushMap.remove(uuid), throwable -> {
            if (throwable == null) {
                audience.sendMessage(Component.translatable("command.push.published"));
            } else {
                audience.sendMessage(Component.translatable("command.push.error").arguments(
                        Component.text(throwable.getMessage())
                ));
            }
        });
    }

    private void publishMessage(PushDetails pushDetails, Consumer<Throwable> consumer) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(pushDetails.uuid());
        if (player == null) return;

        Scheduler.runAsync(() -> {
            DiscordWebhook discordWebhook = new DiscordWebhook(pushConfiguration.url());

            try {
                discordWebhook.addEmbed(embedObject -> {
                    embedObject.setTitle(pushConfiguration.content());
                    embedObject.setColor(pushConfiguration.colorByHex());
                    embedObject.addField(
                            "Von",
                            player.getName(),
                            true
                    );
                    embedObject.addField(
                            "Umgebung",
                            pushConfiguration.environment().display(),
                            true
                    );
                    embedObject.addField(
                            "Änderungen",
                            pushDetails.message(),
                            false
                    );
                    embedObject.setFooter(
                            pushConfiguration.footerMessage(),
                            pushConfiguration.logoUrl()
                    );
                    return embedObject;
                }).execute();
                consumer.accept(null);
            } catch (Exception e) {
                consumer.accept(e);
            }
        });
    }

    public record PushDetails(
            UUID uuid,
            String message
    ) {
    }
}