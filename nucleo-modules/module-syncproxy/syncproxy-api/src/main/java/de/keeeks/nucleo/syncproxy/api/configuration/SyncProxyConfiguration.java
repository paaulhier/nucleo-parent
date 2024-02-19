package de.keeeks.nucleo.syncproxy.api.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class SyncProxyConfiguration {
    private final List<MotdConfiguration> motdConfigurations = new LinkedList<>();
    private final List<UUID> whitelistedPlayers = new LinkedList<>();

    private final int id;
    private final String name;
    private boolean maintenance;
    private boolean active;
    private String protocolText;
    private int maxPlayers;

    private final Instant createdAt;
    private Instant updatedAt;

    public boolean hasProtocol() {
        return protocolText != null;
    }
}