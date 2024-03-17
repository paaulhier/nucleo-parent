package de.keeeks.nucleo.syncproxy.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SyncProxyConfigurationDeletePacket extends Packet {
    private final int configurationId;
}