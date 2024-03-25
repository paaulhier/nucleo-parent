package de.keeeks.nucleo.modules.automessage.shared.packet;

import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;

public class AutomaticMessageCreatePacket extends AutomaticMessagePacket {
    public AutomaticMessageCreatePacket(AutomaticMessage automaticMessage) {
        super(automaticMessage);
    }
}