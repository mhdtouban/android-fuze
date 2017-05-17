/*
 * Copyright 2017 Kickstarter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ***
 *
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/libs/CurrentUser.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import io.gmas.fuze.commons.models.User;
import io.gmas.fuze.commons.preferences.StringPreferenceType;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class CurrentUser extends CurrentUserType {

    private final StringPreferenceType accessTokenPreference;
    private final StringPreferenceType userPreference;

    private final BehaviorSubject<User> user = BehaviorSubject.create();

    public CurrentUser(final @NonNull StringPreferenceType accessTokenPreference,
                       final @NonNull Gson gson,
                       final @NonNull StringPreferenceType userPreference) {
        this.accessTokenPreference = accessTokenPreference;
        this.userPreference = userPreference;

        Timber.d("accessTokenPreference: %s", accessTokenPreference.get());
        Timber.d("userPreference: %s", userPreference.get());

        user.filter(user -> user != null)
                .subscribe(user -> userPreference.set(gson.toJson(user, User.class)));

        if (userPreference.get() != null) {
            user.onNext(gson.fromJson(userPreference.get(), User.class));
        }

    }

    @Override public @Nullable User getUser() {
        return user.getValue();
    }

    @Override public @NonNull Observable<User> observable() {
        return user;
    }

    @Override public @Nullable String getAccessToken() {
        return accessTokenPreference.get();
    }

    @Override public void refresh(final @NonNull User freshUser) {
        user.onNext(freshUser);
    }

    @Override public void refresh(@NonNull String accessToken) {
        accessTokenPreference.set(accessToken);
    }

    @Override public void login(@NonNull User newUser, @NonNull String accessToken) {
        Timber.d("Login user %s", newUser.email());

        accessTokenPreference.set(accessToken);
        user.onNext(newUser);
    }

    @Override public void logout() {
        Timber.d("Logout current user");

        userPreference.delete();
        accessTokenPreference.delete();
        user.onNext(User.builder().build());
    }

}