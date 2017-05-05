package io.gmas.fuze.commons.session;

import android.content.SharedPreferences;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;

import java.util.Locale;

import io.gmas.fuze.commons.CurrentUserType;
import io.gmas.fuze.commons.Shark;
import io.gmas.fuze.commons.services.ApiUserType;
import io.reactivex.Scheduler;

@AutoValue
public abstract class Session {

    public abstract CurrentUserType currentUser();
    public abstract ApiUserType apiUser();
    public abstract Gson gson();
    public abstract SharedPreferences sharedPreferences();
    public abstract Locale locale();
    public abstract Shark shark();
    public abstract Scheduler scheduler();

    public static Builder builder() {
        return new AutoValue_Session.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder currentUser(CurrentUserType __);
        public abstract Builder apiUser(ApiUserType __);
        public abstract Builder gson(Gson __);
        public abstract Builder sharedPreferences(SharedPreferences __);
        public abstract Builder locale(Locale __);
        public abstract Builder shark(Shark __);
        public abstract Builder scheduler(Scheduler __);
        public abstract Session build();
    }

    public abstract Builder toBuilder();

}