package de.keeeks.nucleo.modules.shared;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.utils.ListModifier;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.economy.api.EconomyApi;
import de.keeeks.nucleo.modules.economy.api.packet.EconomyCreatePacket;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.shared.json.EconomyJsonSerializer;
import de.keeeks.nucleo.modules.shared.packet.EconomyCreatePacketListener;
import de.keeeks.nucleo.modules.shared.packet.EconomyDeletePacketListener;
import de.keeeks.nucleo.modules.shared.packet.user.EconomyUserResetPacketListener;
import de.keeeks.nucleo.modules.shared.packet.user.EconomyUserTransferPacketListener;
import de.keeeks.nucleo.modules.shared.packet.user.EconomyUserUpdateBalancePacketListener;
import de.keeeks.nucleo.modules.shared.sql.EconomyRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class NucleoEconomyApi implements EconomyApi {
    private final NatsConnection natsConnection = ServiceRegistry.service(
            NatsConnection.class
    );

    private final List<Economy> economies = new LinkedList<>();

    private final EconomyRepository economyRepository;

    public NucleoEconomyApi(Module module) {
        this.economyRepository = new EconomyRepository(MysqlConnection.create(
                JsonConfiguration.create(
                        module.dataFolder(),
                        "mysql"
                ).loadObject(MysqlCredentials.class, MysqlCredentials.defaultCredentials())
        ));
        GsonBuilder.registerSerializer(new EconomyJsonSerializer());

        natsConnection.registerPacketListener(
                new EconomyUserResetPacketListener(this),
                new EconomyUserTransferPacketListener(this),
                new EconomyUserUpdateBalancePacketListener(this),
                new EconomyCreatePacketListener(this),
                new EconomyDeletePacketListener(this)
        );

        refresh();
    }

    public void modifyEconomies(ListModifier<Economy> modifier) {
        modifier.modify(economies);
    }

    @Override
    public void refresh() {
        economies.clear();
        economies.addAll(economyRepository.economies());
    }

    @Override
    public List<Economy> economies() {
        return economies;
    }

    @Override
    public Economy create(String name) {
        return economy(name).or(() -> {
            Economy economy = economyRepository.createEconomy(name);
            natsConnection.publishPacket(
                    CHANNEL,
                    new EconomyCreatePacket(economy)
            );
            return Optional.of(economy);
        }).orElseThrow();
    }
}