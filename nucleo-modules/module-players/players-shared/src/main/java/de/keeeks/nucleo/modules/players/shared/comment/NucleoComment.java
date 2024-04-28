package de.keeeks.nucleo.modules.players.shared.comment;

import de.keeeks.nucleo.modules.players.api.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class NucleoComment implements Comment {
    private final String id;
    private final UUID playerId;
    private final UUID creatorId;
    private final Instant createdAt;

    private String content;
    private Instant updatedAt;

    public NucleoComment(String id, UUID playerId, UUID creatorId, String content) {
        this.id = id;
        this.playerId = playerId;
        this.creatorId = creatorId;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @Override
    public Comment content(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NucleoComment that = (NucleoComment) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}