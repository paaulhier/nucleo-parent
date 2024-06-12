package de.keeeks.nucleo.modules.forcedhostconnector.config;

import com.velocitypowered.api.proxy.Player;

public record ForcedHost(
        boolean enabled,
        String forcedHost,
        String serverName,
        String requiredPermission,
        ForcedHostCommand command
) {

    public boolean hasPermission(Player player) {
        return requiredPermission == null || player.hasPermission(requiredPermission);
    }

    public static ForcedHost createDefault() {
        return new ForcedHost(
                false,
                "build.keeeks.de",
                "BauServer",
                "keeeks.buildserver",
                ForcedHostCommand.createDefault()
        );
    }

    public static ForcedHost of(
            boolean enabled,
            String forcedHost,
            String serverName,
            String requiredPermission
    ) {
        return new ForcedHost(
                enabled,
                forcedHost,
                serverName,
                requiredPermission,
                null
        );
    }
}