package de.keeeks.nucleo.modules.players.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.CommandTarget;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NucleoOnlinePlayerExecuteCommandPacket extends Packet {
    private final NucleoOnlinePlayer nucleoOnlinePlayer;
    private final CommandTarget commandTarget;
    private final String command;

    public NucleoOnlinePlayer player() {
        return nucleoOnlinePlayer;
    }
}