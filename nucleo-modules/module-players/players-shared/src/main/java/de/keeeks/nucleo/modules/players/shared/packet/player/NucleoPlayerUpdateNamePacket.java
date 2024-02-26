package de.keeeks.nucleo.modules.players.shared.packet.player;

import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class NucleoPlayerUpdateNamePacket extends NucleoPlayerUpdatePacket {
    private final String oldName;
    private final String newName;

    public NucleoPlayerUpdateNamePacket(NucleoPlayer nucleoPlayer, String oldName, String newName) {
        super(nucleoPlayer);
        this.oldName = oldName;
        this.newName = newName;
    }
}