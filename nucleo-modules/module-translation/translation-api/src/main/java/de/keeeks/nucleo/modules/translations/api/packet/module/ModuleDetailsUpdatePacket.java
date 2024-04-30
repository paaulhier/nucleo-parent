package de.keeeks.nucleo.modules.translations.api.packet.module;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.translations.api.ModuleDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ModuleDetailsUpdatePacket extends Packet {
    private final ModuleDetails moduleDetails;
}