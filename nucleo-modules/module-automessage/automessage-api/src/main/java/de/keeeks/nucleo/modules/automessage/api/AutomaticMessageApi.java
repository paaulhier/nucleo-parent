package de.keeeks.nucleo.modules.automessage.api;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutomaticMessageApi {

    String CHANNEL = "nucleo:automessage";

    void reload();

    List<AutomaticMessage> messages();

    default List<AutomaticMessage> enabledMessages() {
        return messages().stream().filter(AutomaticMessage::enabled).toList();
    }

    default List<AutomaticMessage> disabledMessages() {
        return messages().stream().filter(message -> !message.enabled()).toList();
    }

    default Optional<AutomaticMessage> message(int messageId) {
        return messages().stream().filter(message -> message.id() == messageId).findFirst();
    }

    AutomaticMessage createMessage(Component component, UUID creator);

    AutomaticMessage createMessage(String message, UUID creator);

    void deleteMessage(int messageId);

    void updateMessage(int messageId, Component component, UUID updater);

    void enableMessage(int messageId, UUID updater);

    default void enableMessage(AutomaticMessage message, UUID updater) {
        enableMessage(message.id(), updater);
    }

    void disableMessage(int messageId, UUID updater);

    default void disableMessage(AutomaticMessage message) {
        disableMessage(message.id(), message.lastUpdatedBy());
    }

    default void deleteMessage(AutomaticMessage message) {
        deleteMessage(message.id());
    }
}