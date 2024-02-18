package de.keeeks.nucleo.core.api;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ModuleLogger extends Logger {

    protected ModuleLogger(Module module, Logger logger) {
        super("nucleo/" + module.description().name(), null);
        setParent(logger);
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord record) {
        getParent().log(record);
    }

    public static ModuleLogger create(Module module, Logger logger) {
        return new ModuleLogger(module, logger);
    }
}