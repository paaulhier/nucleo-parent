package de.keeeks.nucleo.syncproxy.api.configuration;

import java.util.List;
import java.util.Optional;

public interface SyncProxyService {

    String CHANNEL = "syncproxy";

    List<SyncProxyConfiguration> configurations();

    Optional<SyncProxyConfiguration> configuration(int id);

    Optional<MotdConfiguration> activeMotdConfiguration();

    Optional<SyncProxyConfiguration> currentActiveConfiguration();

    void activateConfiguration(int id);

    void deleteLocal(int id);

    void deleteNetworkWide(int id);

    void updateConfigurationNetworkWide(SyncProxyConfiguration configuration);

    void updateConfiguration(SyncProxyConfiguration configuration);

}