package io.gmas.fuze;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.gmas.fuze.commons.ApiEndpoint;
import io.gmas.fuze.commons.services.ApiModule;
import io.gmas.fuze.commons.services.interceptors.ApiRequestInterceptor;
import okhttp3.OkHttpClient;

@Module(includes = {FZApplicationModule.class, ApiModule.class})
public class ExternalApplicationModule {

    @Provides
    @Singleton
    @NonNull
    ApiEndpoint provideApiEndpoint() {
        return ApiEndpoint.PRODUCTION;
    }

    @Provides
    @Singleton
    @NonNull
    OkHttpClient provideOkHttpClient(final @NonNull ApiRequestInterceptor apiRequestInterceptor) {

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(apiRequestInterceptor);

        return client.build();
    }

}
