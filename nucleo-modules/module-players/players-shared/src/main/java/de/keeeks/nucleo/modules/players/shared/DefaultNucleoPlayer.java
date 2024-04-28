package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.PlayerService;
import de.keeeks.nucleo.modules.players.api.PropertyHolder;
import de.keeeks.nucleo.modules.players.api.Skin;
import de.keeeks.nucleo.modules.players.api.comment.Comment;
import de.keeeks.nucleo.modules.players.shared.comment.NucleoComment;
import de.keeeks.nucleo.modules.players.shared.sql.CommentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DefaultNucleoPlayer implements NucleoPlayer {
    protected static final PlayerService playerService = ServiceRegistry.service(
            PlayerService.class
    );
    protected final CommentRepository commentRepository = ServiceRegistry.service(
            CommentRepository.class
    );

    protected final PropertyHolder propertyHolder = new DefaultPropertyHolder();

    private final List<Comment> comments = new ArrayList<>();

    protected final UUID uuid;

    protected String name;
    protected Skin skin;
    protected String lastIpAddress;

    protected long onlineTime;

    protected Instant lastLogin;
    protected Instant lastLogout;
    protected Instant createdAt;
    protected Instant updatedAt;

    public DefaultNucleoPlayer(UUID uuid, String name) {
        Instant now = Instant.now();
        this.uuid = uuid;
        this.name = name;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public DefaultNucleoPlayer(
            UUID uuid,
            String name,
            Skin skin,
            String lastIpAddress,
            long onlineTime,
            List<Comment> comments,
            Instant lastLogin,
            Instant lastLogout,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.uuid = uuid;
        this.name = name;
        this.skin = skin;
        this.lastIpAddress = lastIpAddress;
        this.onlineTime = onlineTime;
        this.comments.addAll(comments);
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public NucleoPlayer updateName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public NucleoPlayer updateSkin(String value, String signature) {
        this.skin = new Skin(uuid, value, signature);
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public NucleoPlayer updateLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public List<Comment> comments() {
        return List.copyOf(comments);
    }

    @Override
    public Comment createComment(UUID creatorId, String content) {
        NucleoComment nucleoComment = new NucleoComment(
                RandomStringUtils.randomAlphanumeric(16),
                uuid,
                creatorId,
                content
        );
        comments.add(nucleoComment);
        commentRepository.createComment(nucleoComment);
        playerService.updateNetworkWide(this);
        return nucleoComment;
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.deleteComment(comment);
        comments.remove(comment);
        playerService.updateNetworkWide(this);
    }

    @Override
    public void updateComment(Comment comment, String content) {
        commentRepository.updateComment(comment.content(content));
        comments.remove(comment);
        comments.add(comment);
        playerService.updateNetworkWide(this);
    }

    @Override
    public NucleoPlayer updateOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public PropertyHolder properties() {
        return propertyHolder;
    }

    @Override
    public NucleoPlayer updateLastLogin() {
        this.lastLogin = Instant.now();
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public NucleoPlayer updateLastLogout() {
        this.lastLogout = Instant.now();
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public void update() {
        playerService.updateNetworkWide(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DefaultNucleoPlayer that = (DefaultNucleoPlayer) object;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}