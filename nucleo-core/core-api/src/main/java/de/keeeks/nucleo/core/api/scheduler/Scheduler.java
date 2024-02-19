package de.keeeks.nucleo.core.api.scheduler;

import java.util.concurrent.*;

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

    public static void runAsyncTimer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        scheduledExecutorService.scheduleAtFixedRate(
                runnable,
                delay,
                period,
                timeUnit
        );
    }

    public static void runAsyncTimer(Runnable runnable, long period, TimeUnit timeUnit) {
        runAsyncTimer(
                runnable,
                0,
                period,
                timeUnit
        );
    }

    public static void shutdown() {
        executorService.shutdown();
        scheduledExecutorService.shutdown();
    }
}