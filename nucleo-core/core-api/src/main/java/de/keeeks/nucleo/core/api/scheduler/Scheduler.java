package de.keeeks.nucleo.core.api.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public final class Scheduler {
    private static final ThreadFactory threadFactory = new NucleoThreadFactory();

    private static final ExecutorService executorService = Executors.newCachedThreadPool(threadFactory);
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
            4,
            threadFactory
    );


    public static void runAsync(Runnable runnable) {
        executorService.execute(runnable);
    }

    public static void runAsyncLater(Runnable runnable, long delay) {
        scheduledExecutorService.schedule(
                runnable,
                delay,
                java.util.concurrent.TimeUnit.MILLISECONDS
        );
    }

    public static void runAsyncTimer(Runnable runnable, long delay, long period) {
        scheduledExecutorService.scheduleAtFixedRate(
                runnable,
                delay,
                period,
                java.util.concurrent.TimeUnit.MILLISECONDS
        );
    }

    public static void shutdown() {
        executorService.shutdown();
        scheduledExecutorService.shutdown();
    }
}