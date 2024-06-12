package de.keeeks.nucleo.modules.forcedhostconnector.config;

import com.velocitypowered.api.proxy.Player;

public record ForcedHostCommand(
        String command,
        String[] aliases,
        String permission
) {

    public boolean hasPermission(Player player) {
        return permission == null || player.hasPermission(permission);
    }

    public static ForcedHostCommand createDefault() {
        return new ForcedHostCommand(
                "buildserver",
                new String[]{"bs"},
                "keeeks.buildserver"
        );
    }
}