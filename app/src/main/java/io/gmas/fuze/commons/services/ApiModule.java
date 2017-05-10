package io.gmas.fuze.commons.services;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.gmas.fuze.commons.ApiEndpoint;
import io.gmas.fuze.commons.Build;
import io.gmas.fuze.commons.CurrentUserType;
import io.gmas.fuze.commons.qualifiers.ApiRetrofit;
import io.gmas.fuze.commons.services.deserializers.CommentDeserializer;
import io.gmas.fuze.commons.services.deserializers.parsers.CommentParserType;
import io.gmas.fuze.commons.services.deserializers.parsers.impl.CommentParser;
import io.gmas.fuze.commons.services.interceptors.ApiRequestInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    @Singleton
    @NonNull
    ApiRequestInterceptor provideApiRequestInterceptor(final @NonNull CurrentUserType currentUser,
                                                       final @NonNull Build build,
                                                       final @NonNull Locale locale) {
        return new ApiRequestInterceptor(build, locale, currentUser);
    }

    @Provides
    @Singleton
    @ApiRetrofit
    @NonNull
    Retrofit provideApiRetrofit(final @NonNull ApiEndpoint apiEndpoint,
                                final @NonNull OkHttpClient okHttpClient,
                                final @NonNull Gson gson) {
        return createRetrofit(apiEndpoint.url(), okHttpClient, gson);
    }

    private @NonNull Retrofit createRetrofit(final @NonNull String baseUrl,
                                             final @NonNull OkHttpClient okHttpClient,
                                             final @NonNull Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @NonNull
    ApiUserService provideApiUserService(final @ApiRetrofit @NonNull Retrofit retrofit) {
        return retrofit.create(ApiUserService.class);
    }

    @Provides
    @Singleton
    @NonNull
    ApiUserType provideApiUserType(final @NonNull ApiUserService apiUserService,
                                   final @NonNull Gson gson) {
        return new ApiUser(apiUserService, gson);
    }

    @Provides
    @NonNull
    CommentParserType provideCommentParser() {
        return new CommentParser();
    }

    @Provides
    @NonNull
    CommentDeserializer provideCommentDeserializer(final @NonNull CommentParserType commentParser) {
        return new CommentDeserializer(commentParser);
    }

}
