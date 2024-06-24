package de.keeeks.nucleo.modules.automessage.api;

import net.kyori.adventure.text.Component;

import java.time.Instant;
import java.util.UUID;

public interface AutomaticMessage {

    /**
     * Returns the id of the message
     *
     * @return the id of the message
     */
    int id();

    /**
     * Returns the message of the message
     *
     * @return the message of the message
     */
    Component message();

    /**
     * Sets the message of the message
     *
     * @param message the new message of the message
     * @return the message
     */
    AutomaticMessage message(Component message);

    /**
     * Changes the state of the message to the given state
     *
     * @param state the new state of the message
     * @return the message
     */
    AutomaticMessage enabled(boolean state);

    /**
     * Returns a timestamp when the message was created
     *
     * @return a timestamp when the message was created
     */
    Instant createdAt();

    /**
     * Returns the creator of the message
     *
     * @return the creator of the message
     */
    UUID createdBy();

    /**
     * Returns a timestamp when the message was last updated
     *
     * @return a timestamp when the message was last updated
     */
    Instant updatedAt();

    /**
     * Returns the last updater of the message
     *
     * @return the last updater of the message
     */
    UUID lastUpdatedBy();

    /**
     * Returns the state of the message
     * if true, the message is enabled
     *
     * @return the state of the message
     */
    boolean enabled();

    /**
     * Updates the message with the given updater
     *
     * @param updater the updater of the message
     * @return the updated message
     */
    AutomaticMessage update(UUID updater);
}