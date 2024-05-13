package de.keeeks.nucleo.core.api;

import de.keeeks.nucleo.core.api.logger.NucleoLogger;
import lombok.RequiredArgsConstructor;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class NucleoUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Logger logger = NucleoLogger.logger();

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.log(
                java.util.logging.Level.SEVERE,
                "Uncaught exception in thread %s".formatted(t.getName()),
                e
        );
    }
}