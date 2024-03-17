package de.keeeks.nucleo.syncproxy.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.syncproxy.api.SyncProxyConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SyncProxyConfigurationUpdatePacket extends Packet {
    private final SyncProxyConfiguration syncProxyConfiguration;
}
