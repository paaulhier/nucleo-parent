package de.keeeks.nucleo.modules.translation.server;

import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.translation.server.handler.TranslationHandler;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.web.WebModule;
import de.keeeks.nucleo.modules.web.handler.RequestHandlerRegistrar;

@ModuleDescription(
        name = "translations",
        dependencies = {
                @Dependency(name = "web")
        }
)
public class TranslationServerModule extends WebModule {
    @Override
    public void load() {
        ServiceRegistry.registerService(
                TranslationApi.class,
                new DefaultTranslationApi(this)
        );
    }

    @Override
    public void enable() {
        RequestHandlerRegistrar.register(
                new TranslationHandler()
        );
    }
}