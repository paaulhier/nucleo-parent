package de.keeeks.nucleo.modules.messaging.packet;

public interface GenericListenerOrder {

    /**
     * The priority of the packet listener. The lower the priority, the earlier the listener is called.
     * @return The priority of the packet listener.
     */
    int priority();
}