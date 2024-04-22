package de.keeeks.nucleo.modules.privacy.api;

import java.util.Optional;
import java.util.UUID;

public interface PrivacyApi {

    String CHANNEL = "nucleo:privacy";

    Optional<PrivacyInformation> privacyInformation(UUID playerId);

    PrivacyInformation createPrivacyInformation(UUID playerId);

    void invalidatePrivacyInformation(UUID playerId);

    PrivacyInformation accept(
            PrivacyInformation privacyInformation,
            String playerName,
            String ipAddress
    );

    PrivacyInformation decline(PrivacyInformation privacyInformation);

}