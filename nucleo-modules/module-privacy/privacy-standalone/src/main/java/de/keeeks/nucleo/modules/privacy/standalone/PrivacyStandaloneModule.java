package de.keeeks.nucleo.modules.privacy.standalone;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.shared.NucleoPrivacyApi;

@ModuleDescription(
        name = "privacy",
        depends = {"config", "database-mysql", "messaging"}
)
public class PrivacyStandaloneModule extends Module {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                PrivacyApi.class,
                new NucleoPrivacyApi(this)
        );
    }
}