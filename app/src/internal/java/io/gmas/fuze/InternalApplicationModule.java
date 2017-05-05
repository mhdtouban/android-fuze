package io.gmas.fuze;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.gmas.fuze.commons.ApiEndpoint;
import io.gmas.fuze.commons.services.ApiModule;
import io.gmas.fuze.commons.services.interceptors.ApiRequestInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module(includes = {FZApplicationModule.class, ApiModule.class})
public class InternalApplicationModule {

    @Provides
    @Singleton
    @NonNull
    ApiEndpoint provideApiEndpoint() {
        return ApiEndpoint.DEVELOPMENT;
    }

    @Provides
    @Singleton
    @NonNull
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    @NonNull
    OkHttpClient provideOkHttpClient(final @NonNull ApiRequestInterceptor apiRequestInterceptor,
                                     final @NonNull HttpLoggingInterceptor loggingInterceptor) {

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(apiRequestInterceptor);
        client.addInterceptor(loggingInterceptor);

        return client.build();
    }

}
