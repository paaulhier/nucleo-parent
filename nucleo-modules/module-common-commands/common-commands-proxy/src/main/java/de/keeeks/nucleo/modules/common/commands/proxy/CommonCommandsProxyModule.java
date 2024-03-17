package de.keeeks.nucleo.modules.common.commands.proxy;

import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.proxy.module.ProxyModule;
import de.keeeks.nucleo.modules.common.commands.api.translation.CommonCommandsTranslationRegistry;
import de.keeeks.nucleo.modules.common.commands.proxy.commands.PingCommand;
import de.keeeks.nucleo.modules.common.commands.proxy.packet.listener.ping.PlayerPingRequestPacketListener;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;

@ModuleDescription(
        name = "common-commands",
        depends = {"translations", "messaging"}
)
public class CommonCommandsProxyModule extends ProxyModule {

    @Override
    public void load() {
        TranslationRegistry.initializeRegistry(new CommonCommandsTranslationRegistry(
                this
        ));
    }

    @Override
    public void enable() {
        registerCommands(
                new PingCommand(audiences())
        );

        NatsConnection natsConnection = ServiceRegistry.service(
                NatsConnection.class
        );
        natsConnection.registerPacketListener(
                new PlayerPingRequestPacketListener()
        );
    }
}