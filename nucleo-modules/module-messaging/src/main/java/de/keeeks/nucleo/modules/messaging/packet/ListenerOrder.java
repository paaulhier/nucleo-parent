package de.keeeks.nucleo.modules.messaging.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ListenerOrder implements GenericListenerOrder{
    FIRST(-100),
    EARLY(-50),
    NORMAL(0),
    LATE(50),
    LAST(100);

    private final int priority;
}