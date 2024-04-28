package de.keeeks.nucleo.modules.players.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.translatable;

@Getter
@RequiredArgsConstructor
public enum OnlineState {
    ONLINE(translatable("onlineState.online")),
    AWAY(translatable("onlineState.away")),
    PLAYING(translatable("onlineState.playing")),
    WAITING(translatable("onlineState.waiting")),
    SPECTATING(translatable("onlineState.spectating"));

    private final Component displayName;
}