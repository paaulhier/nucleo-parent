package de.keeeks.nucleo.modules.privacy.shared;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import de.keeeks.nucleo.modules.privacy.api.PrivacyInformation;
import de.keeeks.nucleo.modules.privacy.api.packet.PrivacyInformationDeclinedPacket;
import de.keeeks.nucleo.modules.privacy.shared.json.PrivacyInformationSerializer;
import de.keeeks.nucleo.modules.privacy.api.packet.PrivacyInformationCreatePacket;
import de.keeeks.nucleo.modules.privacy.api.packet.PrivacyInformationUpdatePacket;
import de.keeeks.nucleo.modules.privacy.shared.packet.listener.PrivacyInformationUpdatePacketListener;
import de.keeeks.nucleo.modules.privacy.shared.sql.PrivacyInformationRepository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class NucleoPrivacyApi implements PrivacyApi {
    private final Cache<UUID, PrivacyInformation> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(30))
            .build();
    private final NatsConnection natsConnection = ServiceRegistry.service(
            NatsConnection.class
    );

    private final PrivacyInformationRepository privacyInformationRepository;

    public NucleoPrivacyApi(Module module) {
        this.privacyInformationRepository = new PrivacyInformationRepository(module);
        GsonBuilder.registerSerializer(new PrivacyInformationSerializer());

        natsConnection.registerPacketListener(new PrivacyInformationUpdatePacketListener(this));
    }

    public void modifyCache(Consumer<Cache<UUID, PrivacyInformation>> consumer) {
        consumer.accept(cache);
    }

    @Override
    public Optional<PrivacyInformation> privacyInformation(UUID playerId) {
        return Optional.ofNullable(cache.getIfPresent(playerId)).or(() -> {
            PrivacyInformation privacyInformation = privacyInformationRepository.privacyInformation(playerId);
            if (privacyInformation != null) {
                cache.put(playerId, privacyInformation);
                natsConnection.publishPacket(
                        CHANNEL,
                        new PrivacyInformationUpdatePacket(privacyInformation)
                );
                return Optional.of(privacyInformation);
            }
            return Optional.empty();
        });
    }

    @Override
    public PrivacyInformation createPrivacyInformation(UUID playerId) {
        return privacyInformation(playerId).or(() -> {
            PrivacyInformation privacyInformation = privacyInformationRepository.create(playerId);
            cache.put(playerId, privacyInformation);
            natsConnection.publishPacket(
                    CHANNEL,
                    new PrivacyInformationCreatePacket(privacyInformation)
            );
            return Optional.of(privacyInformation);
        }).orElseThrow();
    }

    @Override
    public PrivacyInformation accept(PrivacyInformation privacyInformation) {
        Scheduler.runAsync(() -> {
            privacyInformationRepository.accept(privacyInformation);
            cache.put(privacyInformation.playerId(), privacyInformation);
            natsConnection.publishPacket(
                    CHANNEL,
                    new PrivacyInformationUpdatePacket(privacyInformation)
            );
        });

        return privacyInformation.accept();
    }

    @Override
    public PrivacyInformation decline(PrivacyInformation privacyInformation) {
        natsConnection.publishPacket(
                CHANNEL,
                new PrivacyInformationDeclinedPacket(privacyInformation.playerId(), privacyInformation)
        );
        return privacyInformation;
    }
}