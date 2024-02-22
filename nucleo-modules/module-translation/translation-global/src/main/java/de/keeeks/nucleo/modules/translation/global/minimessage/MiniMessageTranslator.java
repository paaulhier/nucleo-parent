package de.keeeks.nucleo.modules.translation.global.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public abstract class MiniMessageTranslator implements Translator {
    private final MiniMessage miniMessage;

    public MiniMessageTranslator() {
        this(MiniMessage.miniMessage());
    }

    public MiniMessageTranslator(final @NotNull MiniMessage miniMessage) {
        this.miniMessage = Objects.requireNonNull(miniMessage, "miniMessage");
    }

    protected abstract @Nullable String getMiniMessageString(final @NotNull String key, final @NotNull Locale locale);

    @Override
    public final @Nullable MessageFormat translate(final @NotNull String key, final @NotNull Locale locale) {
        return null;
    }

    @Override
    public @Nullable Component translate(final @NotNull TranslatableComponent component, final @NotNull Locale locale) {
        final String miniMessageString = getMiniMessageString(component.key(), locale);

        if (miniMessageString == null) {
            return null;
        }

        final Component resultingComponent;

        if (component.args().isEmpty()) {
            resultingComponent = miniMessage.deserialize(
                    miniMessageString,
                    new PrefixResolver(() -> translate(
                            Component.translatable("prefix"),
                            locale
                    ))
            );
        } else {
            List<Component> translatedArgs = component.args().stream()
                    .filter(arg -> arg instanceof TranslatableComponent)
                    .map(arg -> translate((TranslatableComponent) arg, locale))
                    .toList();

            resultingComponent = miniMessage.deserialize(
                    miniMessageString,
                    new ArgumentTag(translatedArgs),
                    new PrefixResolver(() -> translate(
                            Component.translatable("prefix"),
                            locale
                    ))
            );
        }

        if (component.children().isEmpty()) {
            return resultingComponent;
        } else {
            return resultingComponent.children(component.children());
        }
    }
}