package de.keeeks.nucleo.modules.players.shared.packet.player;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class NucleoOnlinePlayersResponsePacket extends Packet {
    private final List<NucleoOnlinePlayer> onlinePlayers;
}