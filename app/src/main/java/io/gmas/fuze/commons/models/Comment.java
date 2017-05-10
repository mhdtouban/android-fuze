package io.gmas.fuze.commons.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Comment implements Parcelable {

    public abstract @Nullable Long id();

    public static Builder builder() {
        return new AutoValue_Comment.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long __);
        public abstract Comment build();
    }

    public abstract Builder toBuilder();

}