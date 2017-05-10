package io.gmas.fuze.commons.services.deserializers.parsers.impl;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.List;

import io.gmas.fuze.commons.models.Comment;
import io.gmas.fuze.commons.services.deserializers.parsers.CommentParserType;

public final class CommentParser extends BaseParser<Comment> implements CommentParserType {

    @Override public @NonNull List<Comment> toModels(final @NonNull JsonObject jsonObject) {
        return null;
    }

    @Override public  @NonNull Comment toModel(final @NonNull JsonObject jsonObject) {
        return null;
    }

    @Override public @NonNull Comment parseJson(final @NonNull JsonObject jo) throws Exception {
        return null;
    }

}
