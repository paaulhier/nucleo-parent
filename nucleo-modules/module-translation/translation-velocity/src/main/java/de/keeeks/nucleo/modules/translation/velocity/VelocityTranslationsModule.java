package de.keeeks.nucleo.modules.translation.velocity;

import com.velocitypowered.api.event.Subscribe;
import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationApi;
import de.keeeks.nucleo.modules.translation.shared.translation.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.velocity.commands.ReloadTranslationsCommand;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import lombok.Getter;

@Getter
@ModuleDescription(
        name = "translations",
        description = "The velocity translations module",
        dependencies = {
                @Dependency(name = "config"),
                @Dependency(name = "messaging"),
                @Dependency(name = "database-mysql")
        }
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
    public void enable() {
        registerCommands(new ReloadTranslationsCommand());
    }

    @Override
    public void postStartup() {
        for (Module module : Module.modules()) {
            translationApi.createModule(module.description().name());
            TranslationRegistry.create(module);
        }
    }
}