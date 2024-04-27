package de.keeeks.nucleo.modules.inventory.configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public record LoreLine(
        String text,
        boolean translate
) {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public Component component() {
        return translate ? Component.translatable(text) : miniMessage.deserialize(text);
    }
}