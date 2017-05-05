package io.gmas.fuze.commons.services;

import android.support.annotation.NonNull;

import io.gmas.fuze.commons.services.apiresponses.ErrorEnvelope;

public class ApiException extends ResponseException {
    private final ErrorEnvelope errorEnvelope;

    public ApiException(final @NonNull ErrorEnvelope errorEnvelope,
                        final @NonNull retrofit2.Response response) {
        super(response);
        this.errorEnvelope = errorEnvelope;
    }

    public @NonNull ErrorEnvelope errorEnvelope() {
        return errorEnvelope;
    }
}
