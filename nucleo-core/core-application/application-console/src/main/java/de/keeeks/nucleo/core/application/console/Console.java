package de.keeeks.nucleo.core.application.console;

import de.keeeks.nucleo.core.api.utils.StringUtils;
import de.keeeks.nucleo.core.application.console.completer.CommandsCompleter;
import de.keeeks.nucleo.core.application.console.config.ConsoleConfiguration;
import de.keeeks.nucleo.core.application.console.task.LineReadingThread;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
@Accessors(fluent = true)
public final class Console {
    private final LineReadingThread lineReadingThread = LineReadingThread.create(this);

    private final ConsoleConfiguration consoleConfiguration;

    private LineReader lineReader;
    private Terminal terminal;

    public Console(ConsoleConfiguration consoleConfiguration) {
        this.consoleConfiguration = consoleConfiguration;
        try {
            buildTerminalAndLineReader(consoleConfiguration);

            Runtime.getRuntime().addShutdownHook(new Thread(lineReadingThread::interrupt));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private void buildTerminalAndLineReader(ConsoleConfiguration consoleConfiguration) throws IOException {
        this.terminal = TerminalBuilder.builder()
                .name(consoleConfiguration.terminalName())
                .encoding(StandardCharsets.UTF_8)
                .jansi(true)
                .dumb(true)
                .build();

        this.lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .appName(consoleConfiguration.appName())
                .option(LineReader.Option.AUTO_FRESH_LINE, true)
                .option(LineReader.Option.HISTORY_BEEP, false)
                .completer(new CommandsCompleter())
                .build();
    }

    public void startLineReading() {
        lineReadingThread.start();
    }

    public String readLineOrNull() {
        if (terminal == null) return null;
        try {
            return lineReader.readLine(StringUtils.replace(
                    consoleConfiguration.prompt(),
                    "%user%",
                    System.getProperty("user.name")
            ));
        } catch (UserInterruptException exception) {
            Runtime.getRuntime().exit(1);
        }
        return null;
    }

    public static Console create(ConsoleConfiguration consoleConfiguration) {
        return new Console(consoleConfiguration);
    }

}