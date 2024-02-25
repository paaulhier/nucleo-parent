package de.keeeks.nucleo.modules.translation.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.proxy.registry.GlobalProxyTranslationRegistry;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@ModuleDescription(
        name = "translations",
        description = "The proxy translations module",
        softDepends = "config"
)
public class ProxyTranslationsModule extends ProxyModule {
    private TranslationRegistry translationRegistry;

    @Override
    public void load() {
        translationRegistry = new GlobalProxyTranslationRegistry();
    }
}