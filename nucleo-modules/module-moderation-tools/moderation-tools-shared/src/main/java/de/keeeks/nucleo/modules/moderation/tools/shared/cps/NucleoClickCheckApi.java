package de.keeeks.nucleo.modules.moderation.tools.shared.cps;

import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckApi;
import de.keeeks.nucleo.modules.moderation.tools.cps.ClickCheckInformation;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationCreatePacket;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationDeletePacket;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationRequestPacket;
import de.keeeks.nucleo.modules.moderation.tools.cps.packet.ClickCheckInformationResponsePacket;
import de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener.ClickCheckInformationCreatePacketListener;
import de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener.ClickCheckInformationDeletePacketListener;
import de.keeeks.nucleo.modules.moderation.tools.shared.packetlistener.ClickCheckInformationRequestPacketListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;


public final class NucleoClickCheckApi implements ClickCheckApi {
    private final List<ClickCheckInformation> clickChecks = new LinkedList<>();
    private final List<Consumer<ClickCheckInformation>> createListeners = new LinkedList<>();
    private final List<Consumer<ClickCheckInformation>> deleteListeners = new LinkedList<>();

    private final NatsConnection natsConnection;

    public NucleoClickCheckApi(NatsConnection natsConnection) {
        this.natsConnection = natsConnection;
        natsConnection.request(
                CHANNEL,
                new ClickCheckInformationRequestPacket(),
                ClickCheckInformationResponsePacket.class
        ).whenComplete((clickCheckInformationResponsePacket, throwable) -> {
            if (throwable != null) return;
            clickCheckInformationResponsePacket.clickCheckInformation().forEach(this::cacheLocally);
        });

        natsConnection.registerPacketListener(
                new ClickCheckInformationCreatePacketListener(this),
                new ClickCheckInformationDeletePacketListener(this),
                new ClickCheckInformationRequestPacketListener(this)
        );
    }

    public void cacheLocally(ClickCheckInformation clickCheckInformation) {
        deleteLocally(clickCheckInformation);
        clickChecks.add(clickCheckInformation);
        createListeners.forEach(consumer -> consumer.accept(clickCheckInformation));
    }

    public void deleteLocally(ClickCheckInformation clickCheckInformation) {
        clickChecks.remove(clickCheckInformation);
        deleteListeners.forEach(consumer -> consumer.accept(clickCheckInformation));
    }

    @Override
    public List<ClickCheckInformation> clickChecks() {
        return List.copyOf(clickChecks);
    }

    @Override
    public ClickCheckApi createListener(Consumer<ClickCheckInformation> consumer) {
        this.createListeners.add(consumer);
        return this;
    }

    @Override
    public ClickCheckApi deleteListener(Consumer<ClickCheckInformation> consumer) {
        this.deleteListeners.add(consumer);
        return this;
    }

    @Override
    public Optional<ClickCheckInformation> createClickCheck(UUID viewer, UUID target) {
        return clickCheck(viewer).or(() -> {
            ClickCheckInformation clickCheckInformation = new ClickCheckInformation(viewer, target);
            natsConnection.publishPacket(
                    CHANNEL,
                    new ClickCheckInformationCreatePacket(clickCheckInformation)
            );
            return Optional.of(clickCheckInformation);
        });
    }

    @Override
    public Optional<ClickCheckInformation> clickCheck(UUID viewer) {
        return clickChecks.stream().filter(
                clickCheckInformation -> clickCheckInformation.viewer().equals(viewer)
        ).findFirst();
    }

    @Override
    public List<ClickCheckInformation> clickCheckByTarget(UUID target) {
        return clickChecks.stream().filter(
                clickCheckInformation -> clickCheckInformation.target().equals(target)
        ).toList();
    }

    @Override
    public void removeClickCheck(UUID viewer) {
        clickCheck(viewer).ifPresent(this::removeClickCheck);
    }

    @Override
    public void removeClickCheckByTarget(UUID target) {
        clickCheckByTarget(target).forEach(this::removeClickCheck);
    }

    @Override
    public void removeClickCheck(ClickCheckInformation clickCheckInformation) {
        natsConnection.publishPacket(
                CHANNEL,
                new ClickCheckInformationDeletePacket(clickCheckInformation)
        );
    }
}