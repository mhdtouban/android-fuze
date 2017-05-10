package io.gmas.fuze;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.gmas.fuze.commons.AutoValueAdapterFactory;
import io.gmas.fuze.commons.Build;
import io.gmas.fuze.commons.CurrentUser;
import io.gmas.fuze.commons.CurrentUserType;
import io.gmas.fuze.commons.DateTimeTypeConverter;
import io.gmas.fuze.commons.Shark;
import io.gmas.fuze.commons.SharkTrackingClient;
import io.gmas.fuze.commons.preferences.StringPreference;
import io.gmas.fuze.commons.preferences.StringPreferenceType;
import io.gmas.fuze.commons.qualifiers.AccessTokenPreference;
import io.gmas.fuze.commons.qualifiers.ApplicationContext;
import io.gmas.fuze.commons.qualifiers.UserPreference;
import io.gmas.fuze.commons.services.ApiUserType;
import io.gmas.fuze.commons.services.apiresponses.CommentEnvelope;
import io.gmas.fuze.commons.services.deserializers.CommentDeserializer;
import io.gmas.fuze.commons.session.Session;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

@Module
public class FZApplicationModule {
    private Context context;
    private Application application;

    FZApplicationModule(Context context) {
        this.context = context;
        this.application = (Application) context;
    }

    @Provides
    @NonNull
    @ApplicationContext
    Context provideContext() {
        return context;
    }

    @Provides
    @NonNull
    @ApplicationContext
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    @NonNull
    Resources provideResources(final @ApplicationContext @NonNull Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    @NonNull
    Build provideBuild(final @NonNull PackageInfo packageInfo) {
        return new Build(packageInfo);
    }

    @Provides
    @Singleton
    @NonNull
    PackageInfo providePackageInfo(final @ApplicationContext @NonNull Application application) {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Provides
    @Singleton
    @NonNull
    Locale provideLocale() {
        return Locale.getDefault();
    }

    @Provides
    @Singleton
    @NonNull
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    @AccessTokenPreference
    @NonNull
    StringPreferenceType provideAccessTokenPreference(final @NonNull SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, "access_token");
    }

    @Provides
    @Singleton
    @UserPreference
    @NonNull
    StringPreferenceType provideUserPreference(final @NonNull SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, "user");
    }

    @Provides
    @Singleton
    @NonNull
    Session provideSession(final @NonNull CurrentUserType currentUser,
                           final @NonNull ApiUserType apiUser,
                           final @NonNull Gson gson,
                           final @NonNull SharedPreferences sharedPreferences,
                           final @NonNull Locale locale,
                           final @NonNull Shark shark,
                           final @NonNull Scheduler scheduler) {
        return Session.builder()
                .currentUser(currentUser)
                .apiUser(apiUser)
                .gson(gson)
                .sharedPreferences(sharedPreferences)
                .locale(locale)
                .shark(shark)
                .scheduler(scheduler)
                .build();
    }

    @Provides
    @Singleton
    Scheduler provideScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Singleton
    @NonNull
    Shark provideShark(final @ApplicationContext @NonNull Context context, final @NonNull CurrentUserType currentUser) {
        return new Shark(new SharkTrackingClient(context, currentUser));
    }

    @Provides
    @Singleton
    @NonNull
    CurrentUserType provideCurrentUser(@AccessTokenPreference
                                       final @NonNull StringPreferenceType accessTokenPreference,
                                       final @NonNull Gson gson,
                                       @NonNull @UserPreference
                                       final StringPreferenceType userPreference) {
        return new CurrentUser(accessTokenPreference, gson, userPreference);
    }

    @Provides
    @Singleton
    @NonNull
    Gson provideGson(final @NonNull CommentDeserializer commentDeserializer) {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
                .registerTypeAdapterFactory(new AutoValueAdapterFactory())
                .registerTypeAdapter(CommentEnvelope.class, commentDeserializer)
                .create();
    }

}
