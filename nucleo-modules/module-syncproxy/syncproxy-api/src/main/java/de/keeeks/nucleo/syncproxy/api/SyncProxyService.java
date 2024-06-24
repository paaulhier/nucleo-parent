package de.keeeks.nucleo.syncproxy.api;

import java.util.List;
import java.util.Optional;

public interface SyncProxyService {

    String CHANNEL = "nucleo:syncproxy";

    /**
     * Reloads the configuration from the database.
     */
    void reload();

    /**
     * Reloads the configuration from the database. This will also reload the configuration network wide.
     */
    void reloadNetworkWide();

    /**
     * Get a list of all configurations
     *
     * @return The list of configurations
     */
    List<SyncProxyConfiguration> configurations();

    /**
     * Returns the configuration with the given id
     *
     * @param id The id of the configuration
     * @return The configuration or an empty optional if no configuration with the given id exists
     */
    Optional<SyncProxyConfiguration> configuration(int id);

    /**
     * Returns the configuration with the given name
     *
     * @param name The name of the configuration
     * @return The configuration or an empty optional if no configuration with the given name exists
     */
    Optional<SyncProxyConfiguration> configuration(String name);

    /**
     * Returns the active motd configuration
     *
     * @return The active motd configuration or an empty optional if no active motd configuration exists
     */
    Optional<MotdConfiguration> activeMotdConfiguration();

    /**
     * Returns the active configuration
     *
     * @return The active configuration or an empty optional if no active configuration exists
     */
    Optional<SyncProxyConfiguration> currentActiveConfiguration();

    /**
     * Returns the configuration with the given id
     *
     * @param id The id of the configuration
     */
    void activateConfiguration(int id);

    /**
     * Returns the configuration with the given id
     *
     * @param id The id of the configuration
     */
    void deleteLocal(int id);

    /**
     * Returns the configuration with the given id
     *
     * @param id The id of the configuration
     */
    void deleteNetworkWide(int id);

    /**
     * Returns the configuration with the given id
     *
     * @param configuration The configuration to save
     */
    void updateConfigurationNetworkWide(SyncProxyConfiguration configuration);

    /**
     * Returns the configuration with the given id
     *
     * @param configuration The configuration to save
     */
    void updateConfiguration(SyncProxyConfiguration configuration);

}