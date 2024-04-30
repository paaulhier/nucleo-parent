package de.keeeks.nucleo.modules.translation.velocity;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationApi;
import de.keeeks.nucleo.modules.translation.shared.translation.TranslationRegistry;
import de.keeeks.nucleo.modules.translations.api.ModuleDetails;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import lombok.Getter;

@Getter
@ModuleDescription(
        name = "translations",
        description = "The velocity translations module",
        depends = {"config", "messaging", "database-mysql"}
)
public class VelocityTranslationsModule extends VelocityModule {
    private TranslationApi translationApi;

    @Override
    public void load() {
        this.translationApi = ServiceRegistry.registerService(
                TranslationApi.class,
                new DefaultTranslationApi(this)
        );
    }

    @Override
    public void postStartup() {
        for (Module module : Module.modules()) {
            ModuleDetails moduleDetails = translationApi.createModule(module.description().name());
            logger.info("Created module details (%s) for module %s".formatted(
                    moduleDetails.id(),
                    module.description().name()
            ));

            TranslationRegistry.create(module);
            logger.info("Created translation registry for module " + module.description().name());
        }
    }
}