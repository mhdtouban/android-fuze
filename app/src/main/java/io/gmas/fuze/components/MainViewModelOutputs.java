package io.gmas.fuze.components;

import android.support.annotation.NonNull;
import android.util.Pair;

import io.reactivex.Observable;

public interface MainViewModelOutputs {

    @NonNull Observable<Boolean> setLoginButtonIsEnabled();

    @NonNull Observable<Pair<String, String>> success();

}
