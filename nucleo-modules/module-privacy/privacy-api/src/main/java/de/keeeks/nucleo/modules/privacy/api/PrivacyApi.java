package de.keeeks.nucleo.modules.privacy.api;

import java.util.Optional;
import java.util.UUID;

public interface PrivacyApi {

    String CHANNEL = "nucleo:privacy";

    /**
     * Returns the privacy information for the given player.
     *
     * @param playerId The player id.
     * @return The privacy information.
     */
    Optional<PrivacyInformation> privacyInformation(UUID playerId);

    /**
     * Creates new privacy information for the given player.
     *
     * @param playerId The player id.
     * @return The privacy information.
     */
    PrivacyInformation createPrivacyInformation(UUID playerId);

    /**
     * Invalidates the privacy information for the given player.
     *
     * @param playerId The player id.
     */
    void invalidatePrivacyInformation(UUID playerId);

    /**
     * Accepts the privacy information for the given player.
     *
     * @param privacyInformation The privacy information.
     * @param playerName         The player name.
     * @param ipAddress          The ip address.
     * @return The updated privacy information.
     */
    PrivacyInformation accept(
            PrivacyInformation privacyInformation,
            String playerName,
            String ipAddress
    );

    /**
     * Declines the privacy information for the given player.
     *
     * @param privacyInformation The privacy information.
     * @return The updated privacy information.
     */
    PrivacyInformation decline(PrivacyInformation privacyInformation);

}