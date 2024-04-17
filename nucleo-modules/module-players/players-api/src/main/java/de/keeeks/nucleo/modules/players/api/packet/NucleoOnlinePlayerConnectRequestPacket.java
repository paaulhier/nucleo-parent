package de.keeeks.nucleo.modules.players.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NucleoOnlinePlayerConnectRequestPacket extends Packet {
    private final NucleoOnlinePlayer nucleoOnlinePlayer;
    private final String server;

    public NucleoOnlinePlayer player() {
        return nucleoOnlinePlayer;
    }
}