package de.keeeks.nucleo.modules.privacy.velocity;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.shared.NucleoPrivacyApi;
import de.keeeks.nucleo.modules.privacy.velocity.listener.PrivacyDisconnectListener;
import de.keeeks.nucleo.modules.privacy.velocity.listener.PrivacyLoginListener;

@ModuleDescription(
        name = "privacy",
        depends = {"config", "database-mysql", "messaging"}
)
public class PrivacyVelocityModule extends VelocityModule {
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
                new PrivacyLoginListener(privacyApi),
                new PrivacyDisconnectListener()
        );
    }
}