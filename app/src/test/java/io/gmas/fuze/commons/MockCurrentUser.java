package io.gmas.fuze.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.gmas.fuze.commons.factories.UserFactory;
import io.gmas.fuze.commons.models.User;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MockCurrentUser extends CurrentUserType {
    private final BehaviorSubject<User> user = BehaviorSubject.create();
    private @Nullable String accessToken;

    public MockCurrentUser() {
        user.onNext(UserFactory.creator());
    }

    public MockCurrentUser(final @NonNull User initialUser) {
        user.onNext(initialUser);
    }

    @Override public void login(final @NonNull User newUser, final @NonNull String accessToken) {
        user.onNext(newUser);
        this.accessToken = accessToken;
    }

    @Override public void logout() {
        user.onNext(User.builder().build());
        accessToken = null;
    }

    @Override public void refresh(final @NonNull User freshUser) {
        user.onNext(freshUser);
    }

    @Override public void refresh(final @NonNull String accessToken) {
        this.accessToken = accessToken;
    }

    @Override public @NonNull Observable<User> observable() {
        return user;
    }

    @Override public @Nullable String getAccessToken() {
        return accessToken;
    }

    @Override public @Nullable User getUser() {
        return user.getValue();
    }

}
