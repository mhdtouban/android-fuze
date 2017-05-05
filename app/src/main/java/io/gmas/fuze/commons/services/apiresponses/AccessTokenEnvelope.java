package io.gmas.fuze.commons.services.apiresponses;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import io.gmas.fuze.commons.models.User;

@AutoValue
public abstract class AccessTokenEnvelope {

    public abstract @Nullable User user();
    public abstract @Nullable String accessToken();

    public static Builder builder() {
        return new AutoValue_AccessTokenEnvelope.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder user(User __);
        public abstract Builder accessToken(String __);
        public abstract AccessTokenEnvelope build();
    }

    public abstract Builder toBuilder();

}
