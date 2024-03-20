package de.keeeks.nucleo.modules.automessage.proxy.commands;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bungee.annotation.CommandPermission;

import java.util.List;

@Command({
        "automessage",
        "am",
        "automessages",
        "automaticmessage",
        "automaticmessages",
        "auto-message",
        "auto-messages"
})
@CommandPermission("nucleo.commands.automessage")
public class AutoMessageCommand {
    private final AutomaticMessageApi messageApi = ServiceRegistry.service(AutomaticMessageApi.class);
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);

    @DefaultFor({
            "automessage",
            "am",
            "automessages",
            "automaticmessage",
            "automaticmessages",
            "auto-message",
            "auto-messages"
    })
    public void autoMessageCommand(
            Audience audience
    ) {
        audience.sendMessage(Component.translatable(
                "nucleo.automessage.command.usage"
        ));
    }

    @Subcommand("create")
    @Usage("<message>")
    @CommandPermission("nucleo.commands.automessage.create")
    public void createCommand(
            Audience audience,
            String message
    ) {
        Scheduler.runAsync(() -> {
            AutomaticMessage automaticMessage = messageApi.createMessage(
                    message,
                    audience.get(Identity.UUID).orElseThrow()
            );
            audience.sendMessage(Component.translatable(
                    "nucleo.automessage.command.create",
                    Component.text(automaticMessage.id())
            ));
        });
    }

    @Subcommand("reload")
    @CommandPermission("nucleo.commands.automessage.reload")
    public void reloadCommand(
            Audience audience
    ) {
        messageApi.reload();
        audience.sendMessage(Component.translatable(
                "nucleo.automessage.command.reload"
        ));
    }

    @Subcommand("list")
    public void listCommand(
            Audience audience
    ) {
        List<AutomaticMessage> messages = messageApi.enabledMessages();
        if (messages.isEmpty()) {
            audience.sendMessage(Component.translatable(
                    "nucleo.automessage.command.list.empty"
            ));
            return;
        }
        Scheduler.runAsync(() -> {
            audience.sendMessage(Component.translatable(
                    "nucleo.automessage.command.list.header"
            ));
            for (AutomaticMessage message : messages) {
                String creatorName = playerService.player(message.createdBy()).map(
                        NucleoPlayer::name
                ).orElse("unknown");
                audience.sendMessage(Component.translatable(
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
            Audience audience,
            int messageId
    ) {
        Scheduler.runAsync(() -> messageApi.message(messageId).ifPresentOrElse(automaticMessage -> {
            messageApi.deleteMessage(messageId);
            audience.sendMessage(Component.translatable(
                    "nucleo.automessage.command.delete",
                    Component.text(messageId)
            ));
        }, () -> audience.sendMessage(Component.translatable(
                "nucleo.automessage.command.notfound",
                Component.text(messageId)
        ))));
    }
}