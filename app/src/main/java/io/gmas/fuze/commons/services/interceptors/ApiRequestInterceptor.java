package io.gmas.fuze.commons.services.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Locale;

import io.gmas.fuze.commons.Build;
import io.gmas.fuze.commons.CurrentUserType;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequestInterceptor implements Interceptor {

    private final @NonNull CurrentUserType currentUser;
    private final @NonNull Locale locale;
    private final @NonNull Build build;

    public ApiRequestInterceptor(final @NonNull Build build,
                                 final @NonNull Locale locale,
                                 final @NonNull CurrentUserType currentUser) {
        this.currentUser = currentUser;
        this.locale = locale;
        this.build = build;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        return chain.proceed(request(chain.request()));
    }

    private Request request(final @NonNull Request initialRequest) {

        final Request.Builder requestBuilder = initialRequest.newBuilder()
                .header("Android-App", build.versionCode().toString())
                .header("App-Id", build.applicationId())
                .header("User-Agent", "okhttp")
                .header("Accept", "application/json")
                .header("Accept-Language", locale.getLanguage());

        if (currentUser.getAccessToken() != null) {
            requestBuilder.addHeader("Authorization", currentUser.getAccessToken() );
        }

        return requestBuilder.build();
    }
}
