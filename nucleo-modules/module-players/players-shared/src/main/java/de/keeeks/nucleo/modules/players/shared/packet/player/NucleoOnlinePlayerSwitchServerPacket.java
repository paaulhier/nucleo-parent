package de.keeeks.nucleo.modules.players.shared.packet.player;

import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;

public class NucleoOnlinePlayerSwitchServerPacket extends NucleoOnlinePlayerUpdatePacket{
    public NucleoOnlinePlayerSwitchServerPacket(NucleoOnlinePlayer nucleoOnlinePlayer) {
        super(nucleoOnlinePlayer);
    }
}