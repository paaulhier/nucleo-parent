package de.keeeks.nucleo.modules.notifications.shared.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.shared.repository.NotificationRepository;

import java.time.Duration;
import java.util.UUID;

public final class NotificationStateCache {
    private final NotificationRepository notificationRepository;
    private final Notification notification;

    private final LoadingCache<UUID, Boolean> cache;

    public NotificationStateCache(NotificationRepository notificationRepository, Notification notification) {
        this.notificationRepository = notificationRepository;
        this.notification = notification;

        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(30))
                .build(CacheLoader.from(uuid -> notificationRepository.notificationState(
                        notification.id(),
                        uuid
                )));
    }

    public boolean active(UUID uuid) {
        return cache.getUnchecked(uuid);
    }

    public void active(UUID uuid, boolean active) {
        notificationRepository.notificationState(notification.id(), uuid, active);
        cache.put(uuid, active);
    }

    public void activeWithoutUpdate(UUID uuid, boolean active) {
        cache.put(uuid, active);
    }
}