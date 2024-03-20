package de.keeeks.nucleo.modules.automessage.shared.packet;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;

public class AutomaticMessageUpdatePacket extends AutomaticMessagePacket {
    public AutomaticMessageUpdatePacket(AutomaticMessage automaticMessage) {
        super(automaticMessage);
    }
}