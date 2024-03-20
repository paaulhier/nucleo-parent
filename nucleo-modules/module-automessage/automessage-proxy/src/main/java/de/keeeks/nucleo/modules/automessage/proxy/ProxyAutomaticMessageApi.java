package de.keeeks.nucleo.modules.automessage.proxy;

import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.automessage.api.AutomaticMessage;
import de.keeeks.nucleo.modules.automessage.shared.NucleoAutomaticMessageApi;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyAutomaticMessageApi extends NucleoAutomaticMessageApi {
    private final AtomicInteger messageIndex = new AtomicInteger(0);
    private final BungeeAudiences bungeeAudiences;

    public ProxyAutomaticMessageApi(ProxyModule proxyModule) {
        super();
        this.bungeeAudiences = proxyModule.audiences();

        ProxyServer.getInstance().getScheduler().schedule(
                proxyModule.plugin(),
                this::sendNextMessage,
                0,
                3,
                TimeUnit.MINUTES
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
        bungeeAudiences.players().sendMessage(message.message());
    }
}