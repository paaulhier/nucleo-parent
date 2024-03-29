package de.keeeks.nucleo.modules.translation.velocity;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import lombok.Getter;

@Getter
@ModuleDescription(
        name = "translations",
        description = "The velocity translations module"
)
public class VelocityTranslationsModule extends VelocityModule {

    private TranslationRegistry translationRegistry;

    @Override
    public void load() {
        this.translationRegistry = TranslationRegistry.initializeRegistry(
                new GlobalVelocityTranslationRegistry(this)
        );
    }
}