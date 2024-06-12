package de.keeeks.nucleo.modules.forcedhostconnector.config;

import java.util.List;
import java.util.Optional;

public record ForcedHostConfiguration(List<ForcedHost> forcedHosts) {
    public static ForcedHostConfiguration createDefault() {
        return new ForcedHostConfiguration(List.of(ForcedHost.createDefault()));
    }

    public Optional<ForcedHost> forcedHost(String host) {
        return forcedHosts.stream().filter(
                forcedHost -> forcedHost.forcedHost().equalsIgnoreCase(host)
        ).findFirst();
    }
}