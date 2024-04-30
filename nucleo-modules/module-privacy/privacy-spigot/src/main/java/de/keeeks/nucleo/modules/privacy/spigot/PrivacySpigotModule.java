package de.keeeks.nucleo.modules.privacy.spigot;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.shared.NucleoPrivacyApi;
import de.keeeks.nucleo.modules.privacy.spigot.listener.PrivacyJoinListener;
import de.keeeks.nucleo.modules.privacy.spigot.listener.PrivacyQuitListener;

@ModuleDescription(
        name = "privacy",
        depends = {"config", "database-mysql", "messaging", "karistus"}
)
public class PrivacySpigotModule extends SpigotModule {
    private PrivacyApi privacyApi;

    @Override
    public void load() {
        this.privacyApi = ServiceRegistry.registerService(
                PrivacyApi.class,
                new NucleoPrivacyApi(this)
        );
    }

    @Override
    public void enable() {
        registerListener(
                new PrivacyJoinListener(),
                new PrivacyQuitListener()
        );
    }
}