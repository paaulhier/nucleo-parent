package de.keeeks.nucleo.modules.buildserverconnector.config;

public record BuildServerConnectorConfiguration(
        boolean enabled,
        String forcedHost,
        String serverName,
        String requiredPermission
) {
    public static BuildServerConnectorConfiguration createDefault() {
        return new BuildServerConnectorConfiguration(
                false,
                "build.keeeks.de",
                "BauServer",
                "keeeks.buildserver"
        );
    }
}