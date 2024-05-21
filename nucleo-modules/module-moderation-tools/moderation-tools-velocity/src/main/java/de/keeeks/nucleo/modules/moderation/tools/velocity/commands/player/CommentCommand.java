package de.keeeks.nucleo.modules.moderation.tools.velocity.commands.player;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.lejet.api.NameColorizer;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.Formatter;
import de.keeeks.nucleo.core.api.utils.pagination.PaginationResult;
import de.keeeks.nucleo.modules.players.api.NucleoPlayer;
import de.keeeks.nucleo.modules.players.api.comment.Comment;
import revxrsal.commands.annotation.*;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@Usage("nucleo.moderation.comment.usage")
@Command({"comment"})
@CommandPermission("nucleo.moderation.comment")
public class CommentCommand {

    @AutoComplete("@players")
    @DefaultFor("comment")
    public void comment(Player player, NucleoPlayer nucleoPlayer, @Optional @Default("1") int page) {
        if (nucleoPlayer == null) {
            player.sendMessage(translatable("playerNotFound"));
            return;
        }

        Scheduler.runAsync(() -> {
            List<Comment> comments = nucleoPlayer.comments();
            if (comments.isEmpty()) {
                player.sendMessage(translatable(
                        "nucleo.moderation.comment.list.noComments",
                        NameColorizer.coloredName(nucleoPlayer.uuid())
                ));
                return;
            }

            PaginationResult<Comment> paginatedComments = PaginationResult.create(comments, page, 5);

            player.sendMessage(translatable(
                    "nucleo.moderation.comment.list.header",
                    text(paginatedComments.page()),
                    text(paginatedComments.totalPages()),
                    text(paginatedComments.totalAmount()),
                    NameColorizer.coloredName(nucleoPlayer.uuid())
            ));
            for (Comment comment : paginatedComments.list()) {
                player.sendMessage(translatable(
                        "nucleo.moderation.comment.list.entry",
                        text(comment.id()),
                        NameColorizer.coloredName(comment.creatorId()),
                        NameColorizer.coloredName(comment.playerId()),
                        text(comment.content()),
                        text(Formatter.formatShortDateTime(comment.createdAt()))
                ));
            }
        });
    }

    @AutoComplete("@players")
    @Subcommand({"add", "create"})
    public void addComment(Player player, NucleoPlayer nucleoPlayer, String content) {
        if (nucleoPlayer == null) {
            player.sendMessage(translatable("playerNotFound"));
            return;
        }

        Scheduler.runAsync(() -> {
            Comment comment = nucleoPlayer.createComment(
                    player.getUniqueId(),
                    content
            );

            player.sendMessage(translatable(
                    "nucleo.moderation.comment.add.success",
                    text(comment.id()),
                    NameColorizer.coloredName(comment.playerId())
            ));
        });
    }

    @AutoComplete("@players")
    @Subcommand({"remove", "delete"})
    public void removeComment(Player player, NucleoPlayer nucleoPlayer, String commentId) {
        if (nucleoPlayer == null) {
            player.sendMessage(translatable("playerNotFound"));
            return;
        }

        nucleoPlayer.comment(commentId).ifPresentOrElse(comment -> {
            nucleoPlayer.deleteComment(comment);
            player.sendMessage(translatable(
                    "nucleo.moderation.comment.remove.success",
                    text(comment.id()),
                    NameColorizer.coloredName(comment.playerId())
            ));
        }, () -> player.sendMessage(translatable(
                "nucleo.moderation.comment.remove.notFound",
                text(commentId)
        )));
    }
}