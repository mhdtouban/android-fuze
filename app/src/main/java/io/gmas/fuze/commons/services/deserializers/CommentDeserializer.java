package io.gmas.fuze.commons.services.deserializers;

import android.support.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import io.gmas.fuze.commons.models.Comment;
import io.gmas.fuze.commons.services.apiresponses.CommentEnvelope;
import io.gmas.fuze.commons.services.deserializers.parsers.CommentParserType;

public final class CommentDeserializer implements JsonDeserializer<CommentEnvelope> {

    private final CommentParserType commentParser;

    public CommentDeserializer(final @NonNull CommentParserType commentParser) {
        this.commentParser = commentParser;
    }

    @Override public @NonNull CommentEnvelope deserialize(final @NonNull JsonElement json,
                                                          final @NonNull Type typeOfT,
                                                          final @NonNull JsonDeserializationContext context) throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();

        final List<Comment> comments = commentParser.toModels(jsonObject);
        final Comment comment = commentParser.toModel(jsonObject);

        return CommentEnvelope.builder()
                .comments(comments)
                .comment(comment)
                .build();
    }

}
