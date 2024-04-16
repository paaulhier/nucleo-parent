package de.keeeks.nucleo.modules.common.commands.velocity.cloudnet;

import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.api.NotificationApi;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.events.service.CloudServiceLifecycleChangeEvent;
import eu.cloudnetservice.driver.service.ServiceLifeCycle;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.translatable;

@RequiredArgsConstructor
public class CloudNetServiceEventListener {
    private final NotificationApi notificationApi = ServiceRegistry.service(NotificationApi.class);
    private final Notification notification = notificationApi.createNotification(
            "cloud-notify",
            "Notifies about CloudNet service state changes"
    );

    private final ProxyServer proxyServer;

    @EventListener
    public void handleServiceStart(CloudServiceLifecycleChangeEvent event) {
        if (event.newLifeCycle() == ServiceLifeCycle.RUNNING) {
            sendCloudServiceChange(event, "nucleo.notify.cloud.service-start");
        }
    }

    @EventListener
    public void handleStop(CloudServiceLifecycleChangeEvent event) {
        if (event.newLifeCycle() == ServiceLifeCycle.DELETED) {
            sendCloudServiceChange(event, "nucleo.notify.cloud.service-stop");
        }
    }

    private void sendCloudServiceChange(CloudServiceLifecycleChangeEvent event, String messageKey) {
        proxyServer.getAllPlayers().stream().filter(
                player -> player.hasPermission("nucleo.notify.cloud")
        ).filter(
                player -> notificationApi.notificationActive(notification, player.getUniqueId())
        ).forEach(player -> player.sendMessage(translatable(
                messageKey,
                Component.text(event.serviceInfo().name())
        )));
    }
}