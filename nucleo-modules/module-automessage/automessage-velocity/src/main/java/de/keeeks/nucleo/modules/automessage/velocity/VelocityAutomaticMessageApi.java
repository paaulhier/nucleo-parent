package de.keeeks.nucleo.modules.automessage.velocity;

import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessageApi;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class VelocityAutomaticMessageApi extends NucleoAutomaticMessageApi {
    private final AtomicInteger messageIndex = new AtomicInteger(0);
    private final ProxyServer proxyServer;

    public VelocityAutomaticMessageApi(VelocityModule proxyModule) {
        super();
        this.proxyServer = proxyModule.proxyServer();
        proxyServer.getEventManager().register(
                proxyModule.plugin(),
                ProxyInitializeEvent.class,
                event -> proxyModule.proxyServer().getScheduler().buildTask(
                        proxyModule.plugin(),
                        this::sendNextMessage
                ).repeat(3, TimeUnit.MINUTES).schedule()
        );
    }

    private void sendNextMessage() {
        List<AutomaticMessage> automaticMessages = enabledMessages();
        if (automaticMessages.isEmpty()) {
            return;
        }

        if (messageIndex.get() >= automaticMessages.size()) {
            messageIndex.set(0);
        }

        AutomaticMessage message = automaticMessages.get(messageIndex.getAndIncrement());
        proxyServer.getAllPlayers().forEach(player -> player.sendMessage(message.message()));
    }
}