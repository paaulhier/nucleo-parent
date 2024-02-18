package de.keeeks.nucleo.core.loader;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;

import java.util.concurrent.atomic.AtomicBoolean;

public record ModuleContainer(Module module, AtomicBoolean available) {

    public ModuleDescription description() {
        return module().description();
    }

    public static ModuleContainer create(Module module) {
        return new ModuleContainer(module, new AtomicBoolean(true));
    }
}