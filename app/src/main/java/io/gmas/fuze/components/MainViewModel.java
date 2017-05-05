package io.gmas.fuze.components;

import android.support.annotation.NonNull;
import android.util.Pair;

import io.gmas.fuze.commons.ActivityViewModel;
import io.gmas.fuze.commons.CurrentUserType;
import io.gmas.fuze.commons.Shark;
import io.gmas.fuze.commons.services.ApiUserType;
import io.gmas.fuze.commons.session.Session;
import io.gmas.fuze.commons.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static io.gmas.fuze.commons.rx.transformers.Transformers.combineLatestPair;
import static io.gmas.fuze.commons.rx.transformers.Transformers.takeWhen;

public class MainViewModel extends ActivityViewModel<MainActivity>
        implements MainViewModelInputs, MainViewModelOutputs {

    private final PublishSubject<String> email = PublishSubject.create();
    private final PublishSubject<String> password = PublishSubject.create();
    private final PublishSubject<Boolean> connectClick = PublishSubject.create();

    private final BehaviorSubject<Pair<String, String>> success = BehaviorSubject.create();
    private final BehaviorSubject<Boolean> setLoginButtonIsEnabled = BehaviorSubject.create();

    private final CurrentUserType currentUser;
    private final ApiUserType apiUser;
    private final Shark shark;

    public final MainViewModelInputs inputs = this;
    public final MainViewModelOutputs outputs = this;

    public MainViewModel(final @NonNull Session session) {
        currentUser = session.currentUser();
        apiUser = session.apiUser();
        shark = session.shark();

        final Observable<Pair<String, String>> emailAndPassword = email
                .compose(combineLatestPair(password));

        final Observable<Boolean> isValid = emailAndPassword
                .map(ep -> this.isValid(ep.first, ep.second));

        isValid
                .compose(bindToLifecycle())
                .subscribe(setLoginButtonIsEnabled::onNext);

        emailAndPassword
                .compose(takeWhen(connectClick))
                .compose(bindToLifecycle())
                .subscribe(success::onNext);
    }

    @Override public void email(@NonNull String email) {
        this.email.onNext(email);
    }

    @Override public void password(@NonNull String password) {
        this.password.onNext(password);
    }

    @Override public void onClickConnect() {
        connectClick.onNext(true);
    }

    @Override public @NonNull Observable<Boolean> setLoginButtonIsEnabled() {
        return setLoginButtonIsEnabled;
    }

    @Override public @NonNull Observable<Pair<String, String>> success() {
        return success;
    }

    private boolean isValid(final @NonNull String email, final @NonNull String password) {
        return StringUtils.isEmail(email) && password.length() > 0;
    }

}
