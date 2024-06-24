package de.keeeks.nucleo.modules.syncproxy.velocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;

public class ProxyPingFixListener {

    @Subscribe(order = PostOrder.LAST)
    public void handleLastPing(ProxyPingEvent event) {
        ServerPing ping = event.getPing();
        if (ping.getVersion().getProtocol() == 0) {
            event.setPing(ping
                    .asBuilder()
                    .version(new ServerPing.Version(event.getConnection().getProtocolVersion().getProtocol(), ""))
                    .build());
        }
    }
}