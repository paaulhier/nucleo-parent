package de.keeeks.nucleo.modules.automessage.shared;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.utils.ListModifier;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessageApi;
import de.keeeks.nucleo.modules.automessage.shared.gson.AutomaticMessageSerializer;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageCreatePacket;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageDeletePacket;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageReloadPacket;
import de.keeeks.nucleo.modules.automessage.shared.packet.AutomaticMessageUpdatePacket;
import de.keeeks.nucleo.modules.automessage.shared.packet.listener.AutomaticMessageCreatePacketListener;
import de.keeeks.nucleo.modules.automessage.shared.packet.listener.AutomaticMessageDeletePacketListener;
import de.keeeks.nucleo.modules.automessage.shared.packet.listener.AutomaticMessageReloadPacketListener;
import de.keeeks.nucleo.modules.automessage.shared.packet.listener.AutomaticMessageUpdatePacketListener;
import de.keeeks.nucleo.modules.automessage.shared.sql.AutomaticMessageRepository;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class NucleoAutomaticMessageApi implements AutomaticMessageApi {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final List<AutomaticMessage> messages = new LinkedList<>();
    private final NatsConnection natsConnection = ServiceRegistry.service(
            NatsConnection.class
    );
    private final AutomaticMessageRepository automaticMessageRepository;

    public NucleoAutomaticMessageApi() {
        Module module = Module.module("automessage");
        Logger logger = module.logger();
        natsConnection.registerPacketListener(
                new AutomaticMessageCreatePacketListener(this, logger),
                new AutomaticMessageDeletePacketListener(this, logger),
                new AutomaticMessageUpdatePacketListener(this, logger),
                new AutomaticMessageReloadPacketListener(this, logger)
        );
        MysqlCredentials mysqlCredentials = JsonConfiguration.create(
                module.dataFolder(),
                "sql"
        ).loadObject(
                MysqlCredentials.class,
                MysqlCredentials.defaultCredentials()
        );

        automaticMessageRepository = new AutomaticMessageRepository(MysqlConnection.create(
                mysqlCredentials
        ));
        GsonBuilder.registerSerializer(new AutomaticMessageSerializer());
        loadMessages();
    }

    public void modifyMessages(ListModifier<AutomaticMessage> modifier) {
        modifier.modify(messages);
    }

    public void loadMessages() {
        messages.clear();
        messages.addAll(automaticMessageRepository.automaticMessages());
    }

    @Override
    public void reload() {
        natsConnection.publishPacket(
                CHANNEL,
                new AutomaticMessageReloadPacket()
        );
    }

    @Override
    public List<AutomaticMessage> messages() {
        return List.copyOf(messages);
    }

    @Override
    public void deleteMessage(int messageId) {
        message(messageId).ifPresent(message -> {
            automaticMessageRepository.deleteAutomaticMessage(messageId);
            natsConnection.publishPacket(
                    CHANNEL,
                    new AutomaticMessageDeletePacket(message)
            );
        });
    }

    @Override
    public void enableMessage(int messageId, UUID updater) {
        changeMessageState(
                messageId,
                updater,
                true
        );
    }

    private void changeMessageState(int messageId, UUID updater, boolean state) {
        message(messageId).ifPresent(message -> {
            message.enabled(state).update(updater);
            natsConnection.publishPacket(
                    CHANNEL,
                    new AutomaticMessageUpdatePacket(message)
            );
        });
    }

    @Override
    public void disableMessage(int messageId, UUID updater) {
        changeMessageState(
                messageId,
                updater,
                false
        );
    }

    @Override
    public void updateMessage(int messageId, Component component, UUID updater) {
        message(messageId).ifPresent(message -> {
            message.message(component).update(updater);
            natsConnection.publishPacket(
                    CHANNEL,
                    new AutomaticMessageUpdatePacket(message)
            );
        });
    }

    @Override
    public AutomaticMessage createMessage(Component component, UUID creator) {
        int messageId = automaticMessageRepository.insertAutomaticMessage(
                component,
                creator
        );
        AutomaticMessage automaticMessage = new NucleoAutomaticMessage(
                messageId,
                creator,
                component
        );
        natsConnection.publishPacket(
                CHANNEL,
                new AutomaticMessageCreatePacket(automaticMessage)
        );
        return automaticMessage;
    }

    @Override
    public AutomaticMessage createMessage(String message, UUID creator) {
        return createMessage(
                miniMessage.deserialize(message),
                creator
        );
    }
}