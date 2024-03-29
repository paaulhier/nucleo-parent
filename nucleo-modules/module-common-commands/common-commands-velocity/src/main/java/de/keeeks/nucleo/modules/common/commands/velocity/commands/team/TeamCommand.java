package de.keeeks.nucleo.modules.common.commands.velocity.commands.team;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.permission.PermissionApi;
import de.keeeks.lejet.api.permission.PermissionGroup;
import de.keeeks.lejet.api.permission.PermissionUser;
import de.keeeks.lejet.api.permission.PrefixType;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Command({"team"})
@CommandPermission("nucleo.commands.team")
public class TeamCommand {
    private final PlayerService playerService = ServiceRegistry.service(PlayerService.class);
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final PermissionApi permissionApi = PermissionApi.instance();


    @DefaultFor({"team"})
    public void teamCommand(Player player) {
        player.sendMessage(Component.translatable("nucleo.commands.team.header"));
        playerService.onlinePlayers().stream().map(
                mapToUserMapping()
        ).filter(Objects::nonNull).filter(TeamCommand::isInTeam).sorted(
                sortByGroups()
        ).forEach(nucleoPermissionUserMapping -> {
            NucleoOnlinePlayer onlinePlayer = nucleoPermissionUserMapping.nucleoOnlinePlayer();
            PermissionUser permissionUser = nucleoPermissionUserMapping.permissionUser();
            Optional<PermissionGroup> highestPermissionGroup = permissionUser.highestPermssionGroup();
            player.sendMessage(Component.translatable("nucleo.commands.team.entry").arguments(
                    highestPermissionGroup.map(
                            permissionGroup -> permissionGroup.prefix(PrefixType.DISPLAY)
                    ).orElse(Component.text("???")),
                    Component.text(onlinePlayer.name()),
                    miniMessage.deserialize(highestPermissionGroup.map(
                            PermissionGroup::color
                    ).orElse("#ffffff") + onlinePlayer.name()),
                    Component.text(onlinePlayer.server()),
                    Component.text(onlinePlayer.proxy()),
                    Component.text(Formatter.formatLongTime(Duration.between(
                            onlinePlayer.lastLogin(),
                            Instant.now()
                    ).toMillis())))
            );
        });
    }

    private static boolean isInTeam(NucleoPermissionUserMapping nucleoPermissionUserMapping) {
        return nucleoPermissionUserMapping.permissionUser().hasPermission("team.team");
    }

    @NotNull
    private Function<NucleoOnlinePlayer, NucleoPermissionUserMapping> mapToUserMapping() {
        return nucleoOnlinePlayer -> {
            Optional<PermissionUser> userOptional = permissionApi.user(nucleoOnlinePlayer.uuid());
            return userOptional.map(permissionUser -> new NucleoPermissionUserMapping(
                    nucleoOnlinePlayer,
                    permissionUser
            )).orElse(null);
        };
    }

    private static Comparator<NucleoPermissionUserMapping> sortByGroups() {
        return (o1, o2) -> {
            Integer firstPriority = priorityOfGroup(o1);
            Integer secondPriority = priorityOfGroup(o2);
            return Integer.compare(firstPriority, secondPriority);
        };
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