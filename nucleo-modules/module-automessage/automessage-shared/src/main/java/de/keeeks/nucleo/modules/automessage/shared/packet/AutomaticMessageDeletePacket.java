package de.keeeks.nucleo.modules.automessage.shared.packet;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;

public class AutomaticMessageDeletePacket extends AutomaticMessagePacket {
    public AutomaticMessageDeletePacket(AutomaticMessage automaticMessage) {
        super(automaticMessage);
    }
}