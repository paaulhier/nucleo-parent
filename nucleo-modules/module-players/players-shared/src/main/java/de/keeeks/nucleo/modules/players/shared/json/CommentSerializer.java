package de.keeeks.nucleo.modules.players.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.players.api.comment.Comment;
import de.keeeks.nucleo.modules.players.shared.comment.NucleoComment;

import java.lang.reflect.Type;

public class CommentSerializer extends JsonSerializer<Comment> {
    public CommentSerializer() {
        super(Comment.class);
    }

    @Override
    public Comment deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(
                jsonElement,
                NucleoComment.class
        );
    }

    @Override
    public JsonElement serialize(
            Comment comment,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(
                comment,
                NucleoComment.class
        );
    }
}