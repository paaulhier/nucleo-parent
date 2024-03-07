package de.keeeks.nucleo.syncproxy.api.configuration.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class SyncProxyConfigurationUpdatePacket extends Packet {
    private final SyncProxyConfiguration syncProxyConfiguration;
}
