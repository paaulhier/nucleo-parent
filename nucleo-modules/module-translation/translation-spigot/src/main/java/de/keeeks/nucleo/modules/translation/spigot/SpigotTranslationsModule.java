package de.keeeks.nucleo.modules.translation.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.spigot.protocol.EquipmentTranslationAdapter;
import de.keeeks.nucleo.modules.translation.spigot.protocol.OpenWindowTranslationAdapter;
import de.keeeks.nucleo.modules.translation.spigot.protocol.SetSlotTranslationAdapter;
import de.keeeks.nucleo.modules.translation.spigot.protocol.WindowItemsTranslationAdapter;
import de.keeeks.nucleo.modules.translation.spigot.registry.GlobalSpigotTranslationRegistry;
import lombok.Getter;

import java.util.List;

@Getter
@ModuleDescription(
        name = "translations",
        description = "The spigot translations module",
        depends = {"config"}
)
public class SpigotTranslationsModule extends SpigotModule {
    private TranslationRegistry translationRegistry;

    @Override
    public void load() {
        translationRegistry = new GlobalSpigotTranslationRegistry();
    }

    @Override
    public void enable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        List.of(
                new EquipmentTranslationAdapter(this),
                new OpenWindowTranslationAdapter(this),
                new SetSlotTranslationAdapter(this),
                new WindowItemsTranslationAdapter(this)
        ).forEach(protocolManager::addPacketListener);
    }
}