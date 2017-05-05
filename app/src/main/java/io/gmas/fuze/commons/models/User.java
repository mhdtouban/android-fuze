package io.gmas.fuze.commons.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import io.gmas.fuze.commons.qualifiers.AutoGson;

@AutoGson
@AutoValue
public abstract class User implements Parcelable {

    public abstract @Nullable Long id();
    public abstract @Nullable String firstName();
    public abstract @Nullable String lastName();
    public abstract @Nullable String birthDate();
    public abstract @Nullable String email();
    public abstract @Nullable String gender();
    public abstract @Nullable Integer country();

    public static Builder builder() {
        return new AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long __);
        public abstract Builder firstName(String __);
        public abstract Builder lastName(String __);
        public abstract Builder birthDate(String __);
        public abstract Builder email(String __);
        public abstract Builder gender(String __);
        public abstract Builder country(Integer __);
        public abstract User build();
    }

    public abstract Builder toBuilder();

}
