package de.keeeks.nucleo.modules.syncproxy.sql;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.syncproxy.sql.transformer.MotdConfigurationTransformer;
import de.keeeks.nucleo.modules.syncproxy.sql.transformer.SyncProxyConfigurationTransformer;
import de.keeeks.nucleo.syncproxy.api.configuration.MotdConfiguration;
import de.keeeks.nucleo.syncproxy.api.configuration.SyncProxyConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class SyncProxyConfigurationRepository {
    private final SyncProxyConfigurationTransformer syncProxyConfigurationTransformer = new SyncProxyConfigurationTransformer();
    private final MotdConfigurationTransformer motdConfigurationTransformer = new MotdConfigurationTransformer(
            MiniMessage.miniMessage()
    );

    private final MysqlConnection mysqlConnection;

    public SyncProxyConfigurationRepository(MysqlCredentials mysqlCredentials) {
        this.mysqlConnection = MysqlConnection.create(mysqlCredentials);
    }

    public List<SyncProxyConfiguration> configurations() {
        return mysqlConnection.queryList(
                "select * from syncProxy_configurations;",
                syncProxyConfigurationTransformer
        ).stream().peek(syncProxyConfiguration -> syncProxyConfiguration.motdConfigurations().addAll(
                motdConfigurations(syncProxyConfiguration.id())
        )).toList();
    }

    public List<MotdConfiguration> motdConfigurations(int configurationId) {
        return mysqlConnection.queryList(
                "select * from syncProxy_motds where configurationId = ?;",
                statement -> statement.setInt(1, configurationId),
                motdConfigurationTransformer
        );
    }

    public void activateConfiguration(int id) {
        mysqlConnection.prepare(
                "update syncProxy_configurations set active = false;"
        );
        mysqlConnection.prepare(
                "update syncProxy_configurations set active = true where id = ?;",
                statement -> statement.setInt(1, id)
        );
    }
}