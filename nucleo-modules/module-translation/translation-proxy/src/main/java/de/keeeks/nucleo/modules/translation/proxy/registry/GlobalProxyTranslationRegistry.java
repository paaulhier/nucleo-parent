package de.keeeks.nucleo.modules.translation.proxy.registry;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translation.proxy.ProxyTranslationsModule;

public class GlobalProxyTranslationRegistry extends TranslationRegistry {

    public GlobalProxyTranslationRegistry() {
        super(Module.module(ProxyTranslationsModule.class));
    }
}