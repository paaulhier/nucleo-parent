package de.keeeks.nucleo.modules.players.api.packet;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.players.api.NucleoOnlinePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NucleoOnlinePlayerConnectResponsePacket extends Packet {
    private final NucleoOnlinePlayer nucleoOnlinePlayer;
    private final String server;
    private final State state;

    public boolean success() {
        return state == State.SUCCESS;
    }

    public enum State {
        SUCCESS,
        ALREADY_CONNECTED,
        CONNECTION_IN_PROGRESS,
        CONNECTION_CANCELLED,
        SERVER_DISCONNECTED,
        SERVER_NOT_FOUND;

        public static State from(String name) {
            for (State value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }

        public boolean successful() {
            return this == SUCCESS;
        }
    }
}