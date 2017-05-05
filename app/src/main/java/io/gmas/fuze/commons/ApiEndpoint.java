package io.gmas.fuze.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.gmas.fuze.commons.utils.Secrets;

public enum ApiEndpoint {
    PRODUCTION("Production", Secrets.Api.Endpoint.PRODUCTION),
    DEVELOPMENT("Development", Secrets.Api.Endpoint.DEVELOPMENT),
    CUSTOM("Custom", null);

    private String name;
    private String url;

    ApiEndpoint(final @NonNull String name, final @Nullable String url) {
        this.name = name;
        this.url = url;
    }

    public
    @NonNull
    String url() {
        return url;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ApiEndpoint from(final @NonNull String url) {
        for (final ApiEndpoint value : values()) {
            if (value.url != null && value.url.equals(url)) {
                return value;
            }
        }
        final ApiEndpoint endpoint = CUSTOM;
        endpoint.url = url;
        return endpoint;
    }
}
