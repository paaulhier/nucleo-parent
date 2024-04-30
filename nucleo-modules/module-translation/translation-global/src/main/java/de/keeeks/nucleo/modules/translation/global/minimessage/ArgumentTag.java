package de.keeeks.nucleo.modules.translation.global.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@Deprecated
public class ArgumentTag implements TagResolver {
    private static final String NAME = "argument";
    private static final String NAME_1 = "arg";

    private final List<? extends ComponentLike> argumentComponents;

    public ArgumentTag(final @NotNull List<? extends ComponentLike> argumentComponents) {
        this.argumentComponents = Objects.requireNonNull(
                argumentComponents,
                "argumentComponents"
        );
    }

    @Override
    public @Nullable Tag resolve(
            final @NotNull String name,
            final @NotNull ArgumentQueue arguments,
            final @NotNull Context ctx
    ) throws ParsingException {
        if (!has(name)) {
            return null;
        }

        final int index = arguments.popOr(
                "No argument number provided"
        ).asInt().orElseThrow(
                () -> ctx.newException("Invalid argument number", arguments)
        );

        if (index < 0 || index >= argumentComponents.size()) {
            return Tag.selfClosingInserting(Component.text("<missing argument %s>".formatted(
                    index
            )));
        }

        return Tag.selfClosingInserting(argumentComponents.get(index));
    }

    @Override
    public boolean has(final @NotNull String name) {
        return name.equals(NAME) || name.equals(NAME_1);
    }
}