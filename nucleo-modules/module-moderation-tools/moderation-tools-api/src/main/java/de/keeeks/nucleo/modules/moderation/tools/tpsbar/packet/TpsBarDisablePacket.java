package de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet;

import java.util.UUID;

public class TpsBarDisablePacket extends TpsBarUpdateStatePacket {
    public TpsBarDisablePacket(UUID uuid) {
        super(uuid, false);
    }
}