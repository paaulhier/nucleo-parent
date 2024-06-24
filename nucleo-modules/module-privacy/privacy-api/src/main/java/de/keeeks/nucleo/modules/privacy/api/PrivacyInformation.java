package de.keeeks.nucleo.modules.privacy.api;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PrivacyInformation {

    /**
     * Returns the id of the privacy information.
     *
     * @return The id of the privacy information.
     */
    int id();

    /**
     * Returns the id of the player.
     *
     * @return The id of the player.
     */
    UUID playerId();

    /**
     * Returns the name of the player.
     *
     * @return The name of the player.
     */
    String playerName();

    /**
     * Returns whether the privacy information has been accepted.
     *
     * @return Whether the privacy information has been accepted.
     */
    boolean accepted();

    /**
     * Accepts the privacy information for the given player.
     * Due to DSGVO, the player has to accept the privacy information.
     * Data like the IP address needs to be stored in the privacy information then.
     *
     * @param playerName The name of the accepting player.
     * @param ipAddress  The IP address of the accepting player.
     * @return The updated privacy information.
     */
    PrivacyInformation accept(
            String playerName,
            String ipAddress
    );

    /**
     * Returns the ip address of the player that accepted the privacy information.
     * This is only available if the privacy information has been accepted.
     *
     * @return The ip address of the player that accepted the privacy information.
     */
    Optional<String> ipAddress();

    /**
     * Returns the creation date of the privacy information.
     *
     * @return The creation date of the privacy information.
     */
    Instant createdAt();

    /**
     * Returns the date when the privacy information has been accepted.
     *
     * @return The date when the privacy information has been accepted.
     */
    Instant acceptedAt();

}