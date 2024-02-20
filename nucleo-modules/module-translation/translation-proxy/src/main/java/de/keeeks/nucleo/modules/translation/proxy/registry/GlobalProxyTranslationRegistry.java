package de.keeeks.nucleo.modules.translation.proxy.registry;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.translation.proxy.ProxyTranslationsModule;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

public class GlobalProxyTranslationRegistry extends TranslationRegistry {

    public GlobalProxyTranslationRegistry() {
        super(Module.module(ProxyTranslationsModule.class));
    }
}