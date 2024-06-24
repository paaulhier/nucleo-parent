package de.keeeks.nucleo.modules.players.api;

import de.keeeks.nucleo.modules.players.api.comment.Comment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NucleoPlayer {

    /**
     * Returns the unique identifier of this player.
     *
     * @return the unique identifier of this player
     */
    UUID uuid();

    /**
     * Returns the name of this player.
     *
     * @return the name of this player
     */
    String name();

    /**
     * Updates the name of this player.
     *
     * @param name the new name of this player
     * @return this player
     */
    NucleoPlayer updateName(String name);

    /**
     * Returns a skin object of this player. See {@link Skin}.
     *
     * @return a skin object of this player
     */
    Skin skin();

    /**
     * Updates the skin of this player.
     *
     * @param value     the new skin value
     * @param signature the new skin signature
     * @return this player
     */
    NucleoPlayer updateSkin(String value, String signature);

    /**
     * Returns the last IP address of this player.
     * If the player is online, the current IP address is returned.
     *
     * @return the last IP address of this player
     */
    String lastIpAddress();

    /**
     * Updates the last IP address of this player.
     *
     * @param lastIpAddress the new last IP address of this player
     * @return this player
     */
    NucleoPlayer updateLastIpAddress(String lastIpAddress);

    /**
     * Updates the skin of this player. The signature is set to null.
     *
     * @param value the new skin value
     * @return this player
     */
    default NucleoPlayer updateSkin(String value) {
        return updateSkin(
                value,
                null
        );
    }

    /**
     * Returns a list of all comments created for this player.
     *
     * @return a list of all comments created for this player
     */
    List<Comment> comments();

    /**
     * Creates a new comment for this player.
     *
     * @param creatorId the unique identifier of the creator of this comment
     * @param content   the content of this comment
     * @return the created comment
     */
    Comment createComment(UUID creatorId, String content);

    /**
     * Returns a comment by its unique identifier.
     *
     * @param commentId the unique identifier of the comment
     * @return the comment with the given unique identifier
     */
    default Optional<Comment> comment(String commentId) {
        return comments().stream().filter(
                comment -> comment.id().equals(commentId)
        ).findFirst();
    }

    /**
     * Deletes a comment.
     *
     * @param comment the comment to delete
     */
    void deleteComment(Comment comment);

    /**
     * Updates the content of a comment.
     *
     * @param comment the comment to update
     * @param content the new content of the comment
     */
    void updateComment(Comment comment, String content);

    /**
     * Returns the online time of this player in milliseconds.
     *
     * @return the online time of this player in milliseconds
     */
    long onlineTime();

    /**
     * Updates the online time of this player.
     *
     * @param onlineTime the new online time of this player
     * @return this player
     */
    NucleoPlayer updateOnlineTime(long onlineTime);

    /**
     * Returns whether the player is online.
     *
     * @return whether the player is online
     */
    default boolean online() {
        return this instanceof NucleoOnlinePlayer;
    }

    /**
     * Returns whether the player is online.
     * This method is deprecated and should not be used.
     * Please use {@link #online()} instead.
     *
     * @return whether the player is online
     */
    @Deprecated
    default boolean isOnline() {
        return this instanceof NucleoOnlinePlayer;
    }

    /**
     * Returns the properties of this player. See {@link PropertyHolder}.
     *
     * @return the properties of this player
     */
    PropertyHolder properties();

    /**
     * Updates the last login date of this player.
     *
     * @return this player
     */
    NucleoPlayer updateLastLogin();

    /**
     * Updates the last logout date of this player.
     *
     * @return this player
     */
    NucleoPlayer updateLastLogout();

    /**
     * Updates the player.
     */
    void update();

    /**
     * Returns the creation date of this player.
     *
     * @return the creation date of this player
     */
    Instant createdAt();

    /**
     * Returns the last login date of this player.
     *
     * @return the last login date of this player
     */
    Instant lastLogin();

    /**
     * Returns the last logout date of this player.
     *
     * @return the last logout date of this player
     */
    Instant lastLogout();

    /**
     * Returns the last update date of this player.
     *
     * @return the last update date of this player
     */
    Instant updatedAt();

}