package de.keeeks.nucleo.modules.automessage.api;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutomaticMessageApi {

    String CHANNEL = "nucleo:automessage";

    /**
     * Reloads the messages from the database
     */
    void reload();

    /**
     * Returns a list of all messages, regardless of their state
     *
     * @return a list of all messages
     */
    List<AutomaticMessage> messages();

    /**
     * Creates a new message with the given component and creator
     *
     * @param component the component of the message
     * @param creator   the creator of the message
     * @return the created message
     */
    AutomaticMessage createMessage(Component component, UUID creator);

    /**
     * Creates a new message with the given message and creator
     *
     * @param message the message of the message
     * @param creator the creator of the message
     * @return the created message
     */
    AutomaticMessage createMessage(String message, UUID creator);

    /**
     * Deletes the message with the given id
     *
     * @param messageId the id of the message to delete
     */
    void deleteMessage(int messageId);

    /**
     * Updates the message with the given id
     *
     * @param messageId the id of the message to update
     * @param component the new component of the message
     * @param updater   the updater of the message
     */
    void updateMessage(int messageId, Component component, UUID updater);

    /**
     * Updates the message with the given id
     *
     * @param messageId the id of the message to update
     * @param updater   the updater of the message
     */
    void enableMessage(int messageId, UUID updater);

    /**
     * Disables the message with the given id
     *
     * @param messageId the id of the message to disable
     * @param updater   the updater of the message
     */
    void disableMessage(int messageId, UUID updater);

    /**
     * Enables the given message
     *
     * @param message the message to enable
     * @param updater the updater of the message
     */
    default void enableMessage(AutomaticMessage message, UUID updater) {
        enableMessage(message.id(), updater);
    }

    /**
     * Returns a list of all enabled messages
     *
     * @return a list of all enabled messages
     */
    default List<AutomaticMessage> enabledMessages() {
        return messages().stream().filter(AutomaticMessage::enabled).toList();
    }

    /**
     * Returns a list of all disabled messages
     *
     * @return a list of all disabled messages
     */
    default List<AutomaticMessage> disabledMessages() {
        return messages().stream().filter(message -> !message.enabled()).toList();
    }

    /**
     * Returns an optional of the message with the given id
     *
     * @param messageId the id of the message
     * @return an optional of the message with the given id
     */
    default Optional<AutomaticMessage> message(int messageId) {
        return messages().stream().filter(message -> message.id() == messageId).findFirst();
    }

    /**
     * Returns an optional of the message with the given id
     *
     * @param message the message
     */
    default void disableMessage(AutomaticMessage message) {
        disableMessage(message.id(), message.lastUpdatedBy());
    }

    /**
     * Deletes the given message
     *
     * @param message the message to delete
     */
    default void deleteMessage(AutomaticMessage message) {
        deleteMessage(message.id());
    }
}