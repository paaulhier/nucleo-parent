package de.keeeks.nucleo.modules.players.api;

import de.keeeks.nucleo.modules.players.api.comment.Comment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NucleoPlayer {

    UUID uuid();

    String name();

    NucleoPlayer updateName(String name);

    Skin skin();

    NucleoPlayer updateSkin(String value, String signature);

    String lastIpAddress();

    NucleoPlayer updateLastIpAddress(String lastIpAddress);

    default NucleoPlayer updateSkin(String value) {
        return updateSkin(
                value,
                null
        );
    }

    List<Comment> comments();

    Comment createComment(UUID creatorId, String content);

    default Optional<Comment> comment(String commentId) {
        return comments().stream().filter(
                comment -> comment.id().equals(commentId)
        ).findFirst();
    }

    void deleteComment(Comment comment);

    void updateComment(Comment comment, String content);

    long onlineTime();

    NucleoPlayer updateOnlineTime(long onlineTime);

    default boolean isOnline() {
        return this instanceof NucleoOnlinePlayer;
    }

    PropertyHolder properties();

    NucleoPlayer updateLastLogin();

    NucleoPlayer updateLastLogout();

    void update();

    Instant createdAt();

    Instant lastLogin();

    Instant lastLogout();

    Instant updatedAt();

}