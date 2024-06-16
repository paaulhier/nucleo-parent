package de.keeeks.nucleo.modules.moderation.tools.tpsbar.packet;

import java.util.UUID;

public class TpsBarEnablePacket extends TpsBarUpdateStatePacket {
    public TpsBarEnablePacket(UUID uuid) {
        super(uuid, true);
    }
}