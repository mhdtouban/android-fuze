package io.gmas.fuze.commons.services.apiresponses;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

import io.gmas.fuze.commons.models.Comment;

@AutoValue
public abstract class CommentEnvelope {

    public abstract @Nullable Comment comment();
    public abstract @NonNull List<Comment> comments();

    public static Builder builder() {
        return new AutoValue_CommentEnvelope.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder comment(Comment __);
        public abstract Builder comments(List<Comment> __);
        public abstract CommentEnvelope build();
    }

    public abstract Builder toBuilder();

    public boolean isCollection() {
        return comments().size() > 0;
    }

    public boolean isResource() {
        return !isCollection();
    }

}
