package de.keeeks.nucleo.modules.moderation.tools.velocity;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.keeeks.nucleo.core.api.Dependency;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.velocity.module.VelocityModule;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastApi;
import de.keeeks.nucleo.modules.moderation.tools.chatclear.ChatClearApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.broadcast.NucleoBroadcastApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.chatclear.NucleoChatClearApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.cps.NucleoClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.shared.tpsbar.NucleoTpsBarApi;
import de.keeeks.nucleo.modules.moderation.tools.tpsbar.TpsBarApi;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.ChatClearCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.ClicksPerSecondCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.TeamCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.administration.*;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.development.TpsBarCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player.AltsCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player.CommentCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player.JumpToCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player.PlayerInfoCommand;
import de.keeeks.nucleo.modules.moderation.tools.velocity.configuration.PushConfiguration;
import de.keeeks.nucleo.modules.moderation.tools.velocity.listener.ModerationToolsPlayerDisconnectListener;
import de.keeeks.nucleo.modules.moderation.tools.velocity.packet.ClearGlobalChatPacketListener;
import de.keeeks.nucleo.modules.moderation.tools.velocity.packet.ClearPlayerChatPacketListener;
import de.keeeks.nucleo.modules.moderation.tools.velocity.packet.ClearServerChatPacketListener;
import de.keeeks.nucleo.modules.moderation.tools.velocity.packetlistener.BroadcastPacketListener;

@ModuleDescription(
        name = "moderation-tools",
        description = "A module for moderation tools like e.g. click checks",
        dependencies = {
                @Dependency(name = "players"),
                @Dependency(name = "messaging"),
                @Dependency(name = "notifications"),
                @Dependency(name = "karistus", required = false)
        }
)
public class ModerationToolsVelocityModule extends VelocityModule {

    @Override
    public void load() {
        NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);
        ServiceRegistry.registerService(
                ClickCheckApi.class,
                new NucleoClickCheckApi(natsConnection)
        );
        ServiceRegistry.registerService(
                ChatClearApi.class,
                new NucleoChatClearApi()
        );
        ServiceRegistry.registerService(
                BroadcastApi.class,
                new NucleoBroadcastApi()
        );
        ServiceRegistry.registerService(
                TpsBarApi.class,
                new NucleoTpsBarApi()
        );

        natsConnection.registerPacketListener(
                new ClearGlobalChatPacketListener(proxyServer),
                new ClearPlayerChatPacketListener(proxyServer),
                new ClearServerChatPacketListener(proxyServer),
                new BroadcastPacketListener(proxyServer)
        );
    }

    @Override
    public void enable() {
        autoCompleter().registerSuggestion(
                "servers",
                (list, commandActor, executableCommand) -> proxyServer.getAllServers().stream().map(
                        registeredServer -> registeredServer.getServerInfo().getName()
                ).toList()
        );
        commandHandler().registerValueResolver(
                RegisteredServer.class,
                valueResolverContext -> {
                    String pop = valueResolverContext.pop();
                    return proxyServer.getServer(pop).orElse(null);
                }
        );

        registerCommands();

        registerListener(new ModerationToolsPlayerDisconnectListener());
    }

    private void registerCommands() {
        boolean configModuleEnabled = Module.isAvailable("config");
        boolean playersModuleEnabled = Module.isAvailable("players");
        boolean lejetModuleEnabled = Module.isAvailable("lejet");
        boolean verificaModuleEnabled = Module.isAvailable("verifica");
        boolean karistusModuleEnabled = Module.isAvailable("karistus");

        registerCommands(
                new ServerCommand(),
                new TpsBarCommand(),
                new BroadcastCommand()
        );

        registerConditionally(() -> configModuleEnabled, new PushCommand(pushConfiguration()));
        registerConditionally(
                () -> playersModuleEnabled && lejetModuleEnabled && verificaModuleEnabled,
                new PlayerInfoCommand()
        );
        registerConditionally(
                () -> playersModuleEnabled && lejetModuleEnabled,
                new ClicksPerSecondCommand(),
                new CommentCommand(),
                new PullCommand(),
                new SendCommand(proxyServer)
        );
        registerConditionally(
                () -> lejetModuleEnabled,
                new TeamCommand()
        );
        registerConditionally(
                () -> playersModuleEnabled && karistusModuleEnabled,
                new AltsCommand()
        );
        registerConditionally(
                () -> playersModuleEnabled,
                new JumpToCommand(), new ChatClearCommand(proxyServer)
        );
    }

    private PushConfiguration pushConfiguration() {
        return JsonConfiguration.create(
                dataFolder(),
                "push"
        ).loadObject(
                PushConfiguration.class,
                PushConfiguration.createDefault()
        );
    }
}