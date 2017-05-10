package io.gmas.fuze.commons.services.deserializers.parsers;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.List;

import io.gmas.fuze.commons.models.Comment;

public interface CommentParserType {

    @NonNull List<Comment> toModels(final @NonNull JsonObject jsonObject);

    @NonNull Comment toModel(final @NonNull JsonObject jsonObject);

}
