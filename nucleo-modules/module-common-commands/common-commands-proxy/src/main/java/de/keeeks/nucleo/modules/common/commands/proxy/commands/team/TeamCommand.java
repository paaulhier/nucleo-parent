package de.keeeks.nucleo.modules.common.commands.proxy.commands.team;

import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionGroup;
import de.keeeks.lejet.api.permission.PermissionUser;
import de.keeeks.lejet.api.permission.PrefixType;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.bungee.annotation.CommandPermission;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Command({"team"})
@CommandPermission("nucleo.commands.team")
public class TeamCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private final PermissionApi permissionApi = PermissionApi.instance();


    @DefaultFor({"team"})
    public void teamCommand(
            Audience audience
    ) {
        audience.sendMessage(Component.translatable("nucleo.commands.team.header"));
        playerService.onlinePlayers().stream().map(nucleoOnlinePlayer -> {
            Optional<PermissionUser> userOptional = permissionApi.user(nucleoOnlinePlayer.uuid());
            return userOptional.map(permissionUser -> new NucleoPermissionUserMapping(
                    nucleoOnlinePlayer,
                    permissionUser
            )).orElse(null);
        }).filter(Objects::nonNull).sorted((o1, o2) -> {
            Integer firstPriority = priorityOfGroup(o1);
            Integer secondPriority = priorityOfGroup(o2);
            return Integer.compare(secondPriority, firstPriority);
        }).forEach(nucleoPermissionUserMapping -> {
            NucleoOnlinePlayer onlinePlayer = nucleoPermissionUserMapping.nucleoOnlinePlayer();
            PermissionUser permissionUser = nucleoPermissionUserMapping.permissionUser();
            audience.sendMessage(Component.translatable("nucleo.commands.team.entry").arguments(
                    permissionUser.highestPermssionGroup().map(
                            permissionGroup -> permissionGroup.prefix(PrefixType.DISPLAY)
                    ).orElse(Component.text("???")),
                    Component.text(onlinePlayer.name()),
                    Component.text(onlinePlayer.server()),
                    Component.text(onlinePlayer.proxy()),
                    Component.text(Formatter.formatLongTime(Duration.between(
                            onlinePlayer.lastLogin(),
                            Instant.now()
                    ).toMillis())))
            );
        });
    }

    @NotNull
    private static Integer priorityOfGroup(NucleoPermissionUserMapping o1) {
        return o1.permissionUser().highestPermssionGroup().map(PermissionGroup::priority).orElse(0);
    }

    public record NucleoPermissionUserMapping(
            NucleoOnlinePlayer nucleoOnlinePlayer,
            PermissionUser permissionUser
    ) {
    }
}