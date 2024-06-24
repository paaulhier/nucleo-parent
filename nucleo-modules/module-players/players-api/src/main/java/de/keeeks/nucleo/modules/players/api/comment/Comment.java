package de.keeeks.nucleo.modules.players.api.comment;

import java.time.Instant;
import java.util.UUID;

public interface Comment {

    /**
     * Returns the unique identifier of this comment.
     * @return the unique identifier of this comment
     */
    String id();

    /**
     * Returns the unique identifier of the player who is the owner of this comment.
     * @return the unique identifier of the player who is the owner of this comment
     */
    UUID playerId();

    /**
     * Returns the unique identifier of the creator of this comment.
     * @return the unique identifier of the creator of this comment
     */
    UUID creatorId();

    /**
     * Returns the content of this comment.
     * @return the content of this comment
     */
    String content();

    /**
     * Returns the creation date of this comment.
     * @return the creation date of this comment
     */
    Instant createdAt();

    /**
     * Returns the last update date of this comment.
     * @return the last update date of this comment
     */
    Instant updatedAt();

    /**
     * Sets the content of this comment.
     * @param content the content of this comment
     * @return this comment
     */
    Comment content(String content);
}