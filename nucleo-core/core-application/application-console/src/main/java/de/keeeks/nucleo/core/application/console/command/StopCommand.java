package de.keeeks.nucleo.core.application.console.command;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "exit", "shutdown", "ichwillraushier");
    }

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
}