package de.keeeks.nucleo.core.api.scheduler;

public interface ExceptionRunnable extends Runnable {
    void runExceptionally() throws Exception;

    @Override
    default void run() {
        try {
            runExceptionally();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}