package de.keeeks.nucleo.modules.players.api.packet;

import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;

public class NucleoOnlinePlayerNetworkJoinPacket extends NucleoOnlinePlayerUpdatePacket {
    public NucleoOnlinePlayerNetworkJoinPacket(NucleoOnlinePlayer nucleoOnlinePlayer) {
        super(nucleoOnlinePlayer);
    }

    public NucleoOnlinePlayer player() {
        return nucleoOnlinePlayer();
    }
}