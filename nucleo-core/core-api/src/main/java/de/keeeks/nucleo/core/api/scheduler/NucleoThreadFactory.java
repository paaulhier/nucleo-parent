package de.keeeks.nucleo.core.api.scheduler;

import de.keeeks.nucleo.core.api.NucleoUncaughtExceptionHandler;
import de.keeeks.nucleo.core.api.logger.NucleoLogger;
import lombok.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class NucleoThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final String threadNameFormat = "nucleo-pool-" + poolNumber.getAndIncrement() + "-thread-%d";

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(runnable, String.format(threadNameFormat, poolNumber.get()));
        thread.setUncaughtExceptionHandler(new NucleoUncaughtExceptionHandler());
        return thread;
    }
}