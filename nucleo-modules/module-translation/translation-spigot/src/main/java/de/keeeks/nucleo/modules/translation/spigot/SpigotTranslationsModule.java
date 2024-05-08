package de.keeeks.nucleo.modules.translation.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationApi;
import de.keeeks.nucleo.modules.translation.shared.translation.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.spigot.protocol.EquipmentTranslationAdapter;
import de.keeeks.nucleo.modules.translation.spigot.protocol.OpenWindowTranslationAdapter;
import de.keeeks.nucleo.modules.translation.spigot.protocol.SetSlotTranslationAdapter;
import de.keeeks.nucleo.modules.translation.spigot.protocol.WindowItemsTranslationAdapter;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import lombok.Getter;

import java.util.List;

@Getter
@ModuleDescription(
        name = "translations",
        description = "The spigot translations module",
        depends = {"config", "messaging", "database-mysql"}
)
public class SpigotTranslationsModule extends SpigotModule {
    private TranslationApi translationApi;

    @Override
    public void load() {
        this.translationApi = ServiceRegistry.registerService(
                TranslationApi.class,
                new DefaultTranslationApi(this)
        );
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

    @Override
    public void postStartup() {
        for (Module module : Module.modules()) {
            translationApi.createModule(module.description().name());
            TranslationRegistry.create(module);
        }
    }
}