package de.keeeks.nucleo.modules.automessage.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.List;

@Command({"automessage", "am"})
@CommandPermission("nucleo.commands.automessage")
public class AutoMessageCommand {
    private final AutomaticMessageApi messageApi = ServiceRegistry.service(AutomaticMessageApi.class);
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    @DefaultFor("~")
    public void autoMessageCommand(Player player) {
        player.sendMessage(Component.translatable("nucleo.automessage.command.usage"));
    }

    @Subcommand("create")
    @Usage("<message>")
    @CommandPermission("nucleo.commands.automessage.create")
    public void createCommand(
            Player player,
            String message
    ) {
        Scheduler.runAsync(() -> {
            AutomaticMessage automaticMessage = messageApi.createMessage(
                    message,
                    player.getUniqueId()
            );
            player.sendMessage(Component.translatable(
                    "nucleo.automessage.command.create",
                    Component.text(automaticMessage.id())
            ));
        });
    }

    @Subcommand("reload")
    @CommandPermission("nucleo.commands.automessage.reload")
    public void reloadCommand(Player player) {
        messageApi.reload();
        player.sendMessage(Component.translatable("nucleo.automessage.command.reload"));
    }

    @Subcommand("list")
    public void listCommand(Player player) {
        List<AutomaticMessage> messages = messageApi.enabledMessages();
        if (messages.isEmpty()) {
            player.sendMessage(Component.translatable("nucleo.automessage.command.list.empty"));
            return;
        }
        Scheduler.runAsync(() -> {
            player.sendMessage(Component.translatable(
                    "nucleo.automessage.command.list.header"
            ));
            for (AutomaticMessage message : messages) {
                String creatorName = playerService.player(message.createdBy()).map(
                        NucleoPlayer::name
                ).orElse("unknown");
                player.sendMessage(Component.translatable(
                        "nucleo.automessage.command.list.entry",
                        Component.text(message.id()),
                        message.message(),
                        Component.text(Formatter.formatDateTime(message.createdAt())),
                        Component.text(creatorName)
                ));
            }
        });
    }

    @AutoComplete("@autoMessageIds")
    @Usage("<messageId>")
    @Subcommand("delete")
    @CommandPermission("nucleo.commands.automessage.delete")
    public void deleteCommand(
            Player player,
            int messageId
    ) {
        Scheduler.runAsync(() -> messageApi.message(messageId).ifPresentOrElse(automaticMessage -> {
            messageApi.deleteMessage(messageId);
            player.sendMessage(Component.translatable(
                    "nucleo.automessage.command.delete",
                    Component.text(messageId)
            ));
        }, () -> player.sendMessage(Component.translatable(
                "nucleo.automessage.command.notfound",
                Component.text(messageId)
        ))));
    }
}