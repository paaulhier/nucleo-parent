package de.keeeks.nucleo.syncproxy.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class SyncProxyConfigurationDeletePacket extends Packet {
    private final int configurationId;
}