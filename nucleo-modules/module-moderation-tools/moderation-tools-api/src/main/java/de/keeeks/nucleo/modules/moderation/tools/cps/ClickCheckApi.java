package de.keeeks.nucleo.modules.moderation.tools.cps;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface ClickCheckApi {

    String CHANNEL = "clickcheck";

    List<ClickCheckInformation> clickChecks();

    ClickCheckApi createListener(Consumer<ClickCheckInformation> consumer);

    ClickCheckApi deleteListener(Consumer<ClickCheckInformation> consumer);

    default int clickChecksCount() {
        return clickChecks().size();
    }

    Optional<ClickCheckInformation> createClickCheck(UUID viewer, UUID target);

    Optional<ClickCheckInformation> clickCheck(UUID viewer);

    List<ClickCheckInformation> clickCheckByTarget(UUID target);

    void removeClickCheck(UUID viewer);

    void removeClickCheckByTarget(UUID target);

    void removeClickCheck(ClickCheckInformation clickCheckInformation);

}