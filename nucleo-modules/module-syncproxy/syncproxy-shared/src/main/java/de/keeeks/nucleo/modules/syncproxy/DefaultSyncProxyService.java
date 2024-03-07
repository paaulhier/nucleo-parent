package de.keeeks.nucleo.modules.syncproxy;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.syncproxy.api.configuration.packet.SyncProxyConfigurationDeletePacket;
import de.keeeks.nucleo.syncproxy.api.configuration.packet.SyncProxyConfigurationUpdatePacket;
import de.keeeks.nucleo.syncproxy.api.configuration.packet.SyncProxyReloadPacket;
import de.keeeks.nucleo.modules.syncproxy.packetlistener.SyncProxyConfigurationDeletePacketListener;
import de.keeeks.nucleo.modules.syncproxy.packetlistener.SyncProxyConfigurationUpdatePacketListener;
import de.keeeks.nucleo.modules.syncproxy.packetlistener.SyncProxyReloadPacketListener;
import de.keeeks.nucleo.modules.syncproxy.sql.SyncProxyConfigurationRepository;
import de.keeeks.nucleo.syncproxy.api.configuration.MotdConfiguration;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyConfiguration;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyService;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Accessors(fluent = true)
public class DefaultSyncProxyService implements SyncProxyService {

    private final List<SyncProxyConfiguration> syncProxyConfigurations = new LinkedList<>();
    private final NatsConnection natsConnection = ServiceRegistry.service(
            NatsConnection.class
    );
    private final AtomicInteger motdIndex = new AtomicInteger(0);

    private final SyncProxyConfigurationRepository syncProxyConfigurationRepository;
    private final JsonConfiguration jsonConfiguration;

    public DefaultSyncProxyService(Module module) {
        jsonConfiguration = JsonConfiguration.create(
                module.dataFolder(),
                "mysql"
        );
        this.syncProxyConfigurationRepository = new SyncProxyConfigurationRepository(
                jsonConfiguration.loadObject(MysqlCredentials.class, MysqlCredentials.defaultCredentials())
        );

        Scheduler.runAsyncTimer(
                this::incrementMotdIndex,
                30,
                TimeUnit.SECONDS
        );

        syncProxyConfigurations.addAll(syncProxyConfigurationRepository.configurations());

        natsConnection.registerPacketListener(
                new SyncProxyConfigurationUpdatePacketListener(this),
                new SyncProxyConfigurationDeletePacketListener(this),
                new SyncProxyReloadPacketListener(this)
        );
    }

    private void incrementMotdIndex() {
        currentActiveConfiguration().ifPresent(syncProxyConfiguration -> {
            int newIndex = motdIndex.incrementAndGet();
            if (newIndex >= syncProxyConfiguration.motdConfigurations().size()) {
                motdIndex.set(0);
            }
        });
    }

    @Override
    public void reload() {
        synchronized (syncProxyConfigurations) {
            syncProxyConfigurations.clear();
            syncProxyConfigurations.addAll(syncProxyConfigurationRepository.configurations());
            motdIndex.set(0);
        }
    }

    @Override
    public void reloadNetworkWide() {
        natsConnection.publishPacket(
                CHANNEL,
                new SyncProxyReloadPacket()
        );
    }

    @Override
    public Optional<MotdConfiguration> activeMotdConfiguration() {
        return currentActiveConfiguration().flatMap(syncProxyConfiguration -> {
            var motdConfigurations = syncProxyConfiguration.motdConfigurations();
            return Optional.ofNullable(motdConfigurations.get(motdIndex.get()));
        });
    }

    @Override
    public Optional<SyncProxyConfiguration> currentActiveConfiguration() {
        return syncProxyConfigurations.stream().filter(
                SyncProxyConfiguration::active
        ).findFirst();
    }

    @Override
    public List<SyncProxyConfiguration> configurations() {
        return List.copyOf(syncProxyConfigurations);
    }

    @Override
    public Optional<SyncProxyConfiguration> configuration(int id) {
        return syncProxyConfigurations.stream().filter(
                syncProxyConfiguration -> syncProxyConfiguration.id() == id
        ).findFirst();
    }

    @Override
    public Optional<SyncProxyConfiguration> configuration(String name) {
        return syncProxyConfigurations.stream().filter(
                syncProxyConfiguration -> syncProxyConfiguration.name().equals(name)
        ).findFirst();
    }

    @Override
    public void activateConfiguration(int id) {
        configuration(id).ifPresent(syncProxyConfiguration -> {
            syncProxyConfigurations.forEach(configuration -> configuration.active(false));
            syncProxyConfiguration.active(true);

            syncProxyConfigurationRepository.activateConfiguration(id);
            natsConnection.publishPacket(
                    CHANNEL,
                    new SyncProxyConfigurationUpdatePacket(syncProxyConfiguration)
            );
        });
    }

    @Override
    public void deleteLocal(int id) {
        syncProxyConfigurations.removeIf(
                syncProxyConfiguration -> syncProxyConfiguration.id() == id
        );
    }

    @Override
    public void deleteNetworkWide(int id) {
        natsConnection.publishPacket(
                SyncProxyService.CHANNEL,
                new SyncProxyConfigurationDeletePacket(id)
        );
    }

    @Override
    public void updateConfigurationNetworkWide(SyncProxyConfiguration configuration) {
        syncProxyConfigurationRepository.updateConfiguration(configuration);
        natsConnection.publishPacket(
                SyncProxyService.CHANNEL,
                new SyncProxyConfigurationUpdatePacket(configuration)
        );
    }

    @Override
    public void updateConfiguration(SyncProxyConfiguration configuration) {
        deleteLocal(configuration.id());
        syncProxyConfigurations.add(configuration);
    }
}