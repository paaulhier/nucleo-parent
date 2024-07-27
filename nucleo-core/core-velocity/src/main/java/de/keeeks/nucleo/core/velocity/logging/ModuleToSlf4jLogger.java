package de.keeeks.nucleo.core.velocity.logging;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

public class ModuleToSlf4jLogger extends Logger {

    private final org.slf4j.Logger logger;

    public ModuleToSlf4jLogger(org.slf4j.Logger logger) {
        super("", null);
        this.logger = logger;
    }

    @Override
    public void log(LogRecord record) {
        if (record.getLevel().equals(SEVERE)) {
            logger.error(record.getMessage(), record.getThrown());
            return;
        }

        if (record.getLevel().equals(WARNING)) {
            logger.warn(record.getMessage(), record.getThrown());
            return;
        }

        if (record.getLevel().equals(INFO)) {
            logger.info(record.getMessage(), record.getThrown());
            return;
        }

        if (record.getLevel().equals(FINE)) {
            logger.debug(record.getMessage(), record.getThrown());
            return;
        }

        if (record.getLevel().equals(FINER)) {
            logger.trace(record.getMessage(), record.getThrown());
            return;
        }

        if (record.getLevel().equals(FINEST)) {
            logger.trace(record.getMessage(), record.getThrown());
            return;
        }

        throw new IllegalArgumentException("Unknown log level: " + record.getLevel());
    }
}