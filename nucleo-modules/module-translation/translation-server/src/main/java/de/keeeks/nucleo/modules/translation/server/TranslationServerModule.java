package de.keeeks.nucleo.modules.translation.server;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.modules.web.WebModule;

@ModuleDescription(
        name = "translations",
        depends = {"web"}
)
public class TranslationServerModule extends WebModule {
}