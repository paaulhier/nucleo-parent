package de.keeeks.nucleo.modules.notifications.shared;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.utils.ListModifier;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import de.keeeks.nucleo.modules.notifications.api.packet.NotificationCreatePacket;
import de.keeeks.nucleo.modules.notifications.api.packet.NotificationDeletePacket;
import de.keeeks.nucleo.modules.notifications.api.packet.user.NotificationUserUpdateStatePacket;
import de.keeeks.nucleo.modules.notifications.shared.cache.NotificationStateCache;
import de.keeeks.nucleo.modules.notifications.shared.json.NotificationSerializer;
import de.keeeks.nucleo.modules.notifications.shared.packet.listener.NotificationCreatePacketListener;
import de.keeeks.nucleo.modules.notifications.shared.packet.listener.NotificationDeletePacketListener;
import de.keeeks.nucleo.modules.notifications.shared.packet.listener.user.NotificationUserUpdateStatePacketListener;
import de.keeeks.nucleo.modules.notifications.shared.repository.NotificationRepository;

import java.util.*;
import java.util.logging.Level;

public class NucleoNotificationApi implements NotificationApi {
    private final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    private final Map<Integer, NotificationStateCache> notificationStateCaches = new HashMap<>();
    private final List<Notification> notifications = new LinkedList<>();

    private final NotificationRepository notificationRepository;
    private final Module module;

    public NucleoNotificationApi(Module module) {
        this.module = module;
        notificationRepository = new NotificationRepository(mysqlConnection());
        loadNotifications();
        GsonBuilder.registerSerializer(new NotificationSerializer());
        natsConnection.registerPacketListener(
                new NotificationCreatePacketListener(this),
                new NotificationDeletePacketListener(this),
                new NotificationUserUpdateStatePacketListener(this)
        );
    }

    public void modifyState(Notification notification, UUID uuid, boolean state) {
        Optional.ofNullable(notificationStateCaches.get(notification.id())).ifPresent(
                cache -> cache.activeWithoutUpdate(uuid, state)
        );
    }

    private void loadNotifications() {
        notifications.clear();
        notificationStateCaches.clear();
        notifications.addAll(notificationRepository.notifications());
        for (Notification notification : notifications) {
            notificationStateCaches.put(
                    notification.id(),
                    new NotificationStateCache(notificationRepository, notification)
            );
        }
        module.logger().info("Loaded %s notifications".formatted(notifications.size()));
    }

    public void modifyNotifications(ListModifier<Notification> modifier) {
        modifier.modify(notifications);
    }

    @Override
    public Notification createNotification(
            String name,
            String description,
            String requiredPermission
    ) {
        return notification(name).or(() -> {
            int notificationId = notificationRepository.createNotification(
                    name,
                    description
            );

            Notification notification = new NucleoNotification(
                    notificationId,
                    name,
                    description,
                    requiredPermission
            );
            notificationStateCaches.put(
                    notification.id(),
                    new NotificationStateCache(notificationRepository, notification)
            );
            natsConnection.publishPacket(
                    CHANNEL,
                    new NotificationCreatePacket(notification)
            );
            return Optional.of(notification);
        }).orElseThrow();
    }

    @Override
    public List<Notification> notifications() {
        return List.copyOf(notifications);
    }

    @Override
    public void deleteNotification(Notification notification) {
        natsConnection.publishPacket(
                CHANNEL,
                new NotificationDeletePacket(notification)
        );
    }

    @Override
    public boolean notificationActive(Notification notification, UUID uuid) {
        NotificationStateCache stateCache = notificationStateCaches.get(notification.id());
        if (stateCache == null) {
            module.logger().log(
                    Level.WARNING,
                    "No state cache found for notification %s".formatted(notification.name())
            );
            return false;
        }
        return stateCache.active(uuid);
    }

    @Override
    public void notificationActive(Notification notification, UUID uuid, boolean active) {
        NotificationStateCache stateCache = notificationStateCaches.get(notification.id());
        if (stateCache == null) {
            module.logger().log(
                    Level.WARNING,
                    "No state cache found for notification %s".formatted(notification.name())
            );
            return;
        }
        stateCache.active(uuid, active);
        natsConnection.publishPacket(
                CHANNEL,
                new NotificationUserUpdateStatePacket(notification, uuid, active)
        );
        module.logger().info("Set notification %s for %s to %s".formatted(
                notification.name(),
                uuid,
                active
        ));
    }

    private MysqlConnection mysqlConnection() {
        return MysqlConnection.create(JsonConfiguration.create(
                module.dataFolder(),
                "mysql"
        ).loadObject(MysqlCredentials.class, MysqlCredentials.defaultCredentials()));
    }
}