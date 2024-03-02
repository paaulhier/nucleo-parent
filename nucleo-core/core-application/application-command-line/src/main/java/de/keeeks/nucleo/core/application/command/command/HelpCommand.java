package de.keeeks.nucleo.core.application.command.command;

import de.keeeks.nucleo.core.api.ServiceRegistry;

public class HelpCommand extends Command {
    private final CommandRegistry commandRegistry = ServiceRegistry.service(CommandRegistry.class);

    public HelpCommand() {
        super(
                "help",
                "?",
                "hilfe",
                "erion",
                "IchWeißNichtWasIchTunSoll",
                "BitteHelfenSieMirIchBinHierInGefahr"
        );
    }

    @Override
    public void execute(String[] args) {
        var commands = commandRegistry.commands();
        logger.info("&eThe following commands are registered: &f(&6%s&f)".formatted(
                commands.size()
        ));
        commands.stream().map(
                command -> new String[]{
                        command.name(),
                        command.hasAliases() ? " - " + String.join(", ", command.aliases()) : null
                }
        ).forEach(strings -> {
            for (String string : strings) {
                if (string == null) continue;
                logger.info(string);
            }
        });
    }
}