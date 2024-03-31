package de.keeeks.nucleo.modules.players.api.packet;

import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;

public class NucleoOnlinePlayerCreatePacket extends NucleoOnlinePlayerUpdatePacket {
    public NucleoOnlinePlayerCreatePacket(NucleoOnlinePlayer nucleoOnlinePlayer) {
        super(nucleoOnlinePlayer);
    }
}