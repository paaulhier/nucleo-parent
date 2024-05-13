package de.keeeks.nucleo.core.api.logger;

import com.google.common.base.Preconditions;

import java.util.logging.Logger;

public class NucleoLogger extends Logger {
    private static NucleoLogger logger;

    private NucleoLogger(Logger parent) {
        super("Nucleo", null);
        setParent(parent);
    }

    public static NucleoLogger logger() {
        return logger;
    }

    public static NucleoLogger create(Logger parent) {
        return new NucleoLogger(parent);
    }

    public static NucleoLogger create() {
        return new NucleoLogger(null);
    }

    public static void logger(NucleoLogger logger) {
        Preconditions.checkArgument(logger != null, "NucleoLogger already set!");
        NucleoLogger.logger = logger;
    }
}