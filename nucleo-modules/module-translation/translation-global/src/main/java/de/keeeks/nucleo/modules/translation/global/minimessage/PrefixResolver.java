package de.keeeks.nucleo.modules.translation.global.minimessage;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Deprecated
@RequiredArgsConstructor
public class PrefixResolver implements TagResolver {
    private final Supplier<Component> prefixTranslator;

    @Override
    public @Nullable Tag resolve(
            @NotNull String name,
            @NotNull ArgumentQueue arguments,
            @NotNull Context ctx
    ) throws ParsingException {
        if (!has(name)) return null;
        return Tag.selfClosingInserting(prefixTranslator.get());
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equals("prefix");
    }
}