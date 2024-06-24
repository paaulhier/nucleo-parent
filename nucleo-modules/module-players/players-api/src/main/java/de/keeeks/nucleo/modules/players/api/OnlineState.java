package de.keeeks.nucleo.modules.players.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.translatable;

@Getter
@RequiredArgsConstructor
public enum OnlineState {

    /**
     * The player is online. This state is used when the player is connected to a server.
     */
    ONLINE(translatable("onlineState.online")),
    /**
     * The player is away. This state is used when the player is connected to a server but is not actively playing.
     */
    AWAY(translatable("onlineState.away")),
    /**
     * The player is playing. This state is used when the player is connected to a server and is actively playing.
     */
    PLAYING(translatable("onlineState.playing")),
    /**
     * The player is waiting. This state is used when the player is connected to a server but is waiting for a game to start.
     */
    WAITING(translatable("onlineState.waiting")),
    /**
     * The player is spectating. This state is used when the player is connected to a server but is spectating a game.
     */
    SPECTATING(translatable("onlineState.spectating"));

    private final Component displayName;
}