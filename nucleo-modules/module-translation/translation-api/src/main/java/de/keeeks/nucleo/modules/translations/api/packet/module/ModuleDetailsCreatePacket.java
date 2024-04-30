package de.keeeks.nucleo.modules.translations.api.packet.module;

import de.keeeks.nucleo.modules.translations.api.ModuleDetails;

public class ModuleDetailsCreatePacket extends ModuleDetailsUpdatePacket {
    public ModuleDetailsCreatePacket(ModuleDetails moduleDetails) {
        super(moduleDetails);
    }
}