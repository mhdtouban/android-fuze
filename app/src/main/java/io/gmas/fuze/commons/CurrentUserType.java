package io.gmas.fuze.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.gmas.fuze.commons.models.User;
import io.reactivex.Observable;

public abstract class CurrentUserType {

    public abstract void login(final @NonNull User newUser, final @NonNull String accessToken);

    public abstract void logout();

    public abstract void refresh(final @NonNull User freshUser);

    public abstract void refresh(final @NonNull String accessToken);

    public abstract @NonNull Observable<User> observable();

    public abstract @Nullable String getAccessToken();

    public abstract @Nullable User getUser();

}
