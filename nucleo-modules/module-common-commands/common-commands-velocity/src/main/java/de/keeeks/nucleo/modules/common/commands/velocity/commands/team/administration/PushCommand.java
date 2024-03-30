package de.keeeks.nucleo.modules.common.commands.velocity.commands.team.administration;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.discord.DiscordWebhook;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpirationListener;
import de.keeeks.nucleo.core.api.utils.expiringmap.ExpiringMap;
import de.keeeks.nucleo.modules.common.commands.velocity.CommonCommandsVelocityModule;
import de.keeeks.nucleo.modules.common.commands.velocity.configuration.PushConfiguration;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static net.kyori.adventure.text.Component.translatable;

@Command("push")
@CommandPermission("nucleo.team.administration.push")
@RequiredArgsConstructor
public class PushCommand {
    private static final CommonCommandsVelocityModule module = Module.module(CommonCommandsVelocityModule.class);
    private final ExpiringMap<UUID, PushDetails> pushMap = ExpiringMap.builder()
            .expiration(30, TimeUnit.SECONDS)
            .asyncExpirationListener(expirationListener())
            .build();

    @NotNull
    private static ExpirationListener<UUID, PushDetails> expirationListener() {
        return (key, value) -> module.proxyServer().getPlayer(key).ifPresent(
                player -> player.sendMessage(translatable(
                        "command.push.expired"
                ).arguments(
                        Component.text(value.message())
                ))
        );
    }

    private final PushConfiguration pushConfiguration;

    @DefaultFor("push")
    public void pushCommand(
            Player player,
            @Optional String message
    ) {
        if (message == null) {
            player.sendMessage(translatable("command.push.usage"));
            return;
        }
        UUID uuid = player.getUniqueId();

        if (pushMap.containsKey(uuid)) {
            player.sendMessage(translatable("command.push.alreadyPublishing"));
            return;
        }
        player.sendMessage(translatable("command.push.confirm"));
        pushMap.put(
                uuid,
                new PushDetails(uuid, message)
        );
    }

    @Subcommand("confirm")
    public void confirmCommand(Player player) {
        UUID uuid = player.getUniqueId();
        if (!pushMap.containsKey(uuid)) {
            player.sendMessage(translatable("command.push.noPending"));
            return;
        }

        player.sendMessage(translatable("command.push.publishing"));
        publishMessage(pushMap.remove(uuid), throwable -> {
            if (throwable == null) {
                player.sendMessage(translatable("command.push.published"));
            } else {
                player.sendMessage(translatable("command.push.error").arguments(
                        Component.text(throwable.getMessage())
                ));
            }
        });
    }

    private void publishMessage(PushDetails pushDetails, Consumer<Throwable> consumer) {
        module.proxyServer().getPlayer(pushDetails.uuid()).ifPresent(player -> Scheduler.runAsync(() -> {
            DiscordWebhook discordWebhook = new DiscordWebhook(pushConfiguration.url());

            try {
                discordWebhook.addEmbed(embedObject -> {
                    embedObject.setTitle(pushConfiguration.content());
                    embedObject.setColor(pushConfiguration.colorByHex());
                    embedObject.addField(
                            "Von",
                            player.getUsername(),
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
        }));
    }

    public record PushDetails(
            UUID uuid,
            String message
    ) {
    }
}