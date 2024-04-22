package de.keeeks.nucleo.modules.players.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

@Getter
@RequiredArgsConstructor
public class NucleoOnlinePlayerKickPacket extends Packet {
    private final NucleoOnlinePlayer nucleoOnlinePlayer;
    private final Component reason;
    private final boolean raw;

    public NucleoOnlinePlayer player() {
        return nucleoOnlinePlayer;
    }

}