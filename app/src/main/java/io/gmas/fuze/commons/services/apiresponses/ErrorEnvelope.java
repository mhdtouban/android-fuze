package io.gmas.fuze.commons.services.apiresponses;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import io.gmas.fuze.commons.qualifiers.AutoGson;
import io.gmas.fuze.commons.services.ApiException;

@AutoGson
@AutoValue
public abstract class ErrorEnvelope {

    public abstract @Nullable String error();
    public abstract int code();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder error(String __);
        public abstract Builder code(int __);
        public abstract ErrorEnvelope build();
    }

    public static Builder builder() {
        return new AutoValue_ErrorEnvelope.Builder();
    }

    public static @Nullable ErrorEnvelope fromThrowable(final @NonNull Throwable t) {
        if (t instanceof ApiException) {
            final ApiException exception = (ApiException) t;
            return exception.errorEnvelope();
        }

        return null;
    }
}
