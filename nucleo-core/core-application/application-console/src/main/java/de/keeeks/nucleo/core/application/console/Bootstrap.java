package de.keeeks.nucleo.core.application.console;

public final class Bootstrap {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        CoreConsoleApplication coreConsoleApplication = new CoreConsoleApplication();
        coreConsoleApplication.load();
        coreConsoleApplication.enable();
        Runtime.getRuntime().addShutdownHook(new Thread(coreConsoleApplication::disable));
        coreConsoleApplication.logger().info("Startup done. Took %sms".formatted(
                System.currentTimeMillis() - startTime
        ));
    }
}