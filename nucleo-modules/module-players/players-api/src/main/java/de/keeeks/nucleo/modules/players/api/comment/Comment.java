package de.keeeks.nucleo.modules.players.api.comment;

import java.time.Instant;
import java.util.UUID;

public interface Comment {
    String id();
    UUID playerId();
    UUID creatorId();
    String content();
    Instant createdAt();
    Instant updatedAt();

    Comment content(String content);
}