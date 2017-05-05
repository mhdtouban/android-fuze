package io.gmas.fuze.components;

import android.support.annotation.NonNull;

public interface MainViewModelInputs {

    void email(final @NonNull String email);

    void password(final @NonNull String password);

    void onClickConnect();

}
