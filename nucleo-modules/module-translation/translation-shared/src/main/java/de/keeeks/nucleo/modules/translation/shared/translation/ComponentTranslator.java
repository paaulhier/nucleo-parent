package de.keeeks.nucleo.modules.translation.shared.translation;

import de.keeeks.nucleo.modules.translation.shared.translation.minimessage.MiniMessageTranslator;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.BiFunction;

public class ComponentTranslator extends MiniMessageTranslator {
    private final BiFunction<String, Locale, String> messageFormatAdapter;

    public ComponentTranslator(BiFunction<String, Locale, String> messageFormatAdapter) {
        this.messageFormatAdapter = messageFormatAdapter;
    }

    @Override
    public @NotNull Key name() {
        return Key.key(
                "i18n",
                "main"
        );
    }

    @Override
    protected @Nullable String getMiniMessageString(@NotNull String key, @NotNull Locale locale) {
        return messageFormatAdapter.apply(key, locale);
    }
}