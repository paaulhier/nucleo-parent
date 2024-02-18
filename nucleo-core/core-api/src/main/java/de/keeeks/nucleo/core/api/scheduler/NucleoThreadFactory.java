package de.keeeks.nucleo.core.api.scheduler;

import lombok.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class NucleoThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(0);

    private final String threadNameFormat = "nucleo-pool-" + poolNumber.getAndIncrement() + "-thread-%d";

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        return new Thread(runnable, String.format(threadNameFormat, poolNumber.get()));
    }
}