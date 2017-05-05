package io.gmas.fuze.commons.rx.transformers;

import android.support.annotation.Nullable;

import io.gmas.fuze.commons.services.apiresponses.ErrorEnvelope;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;

public final class NeverApiErrorTransformer<T> implements ObservableTransformer<T, T> {

    private final @Nullable Consumer<ErrorEnvelope> errorAction;

    protected NeverApiErrorTransformer() {
        this.errorAction = null;
    }

    protected NeverApiErrorTransformer(final @Nullable Consumer<ErrorEnvelope> errorAction) {
        this.errorAction = errorAction;
    }

    @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnError(e -> {
                    final ErrorEnvelope env = ErrorEnvelope.fromThrowable(e);
                    if (env != null && errorAction != null) {
                        errorAction.accept(env);
                    }
                })
                .onErrorResumeNext(e -> {
                    if (ErrorEnvelope.fromThrowable(e) == null) {
                        return Observable.error(e);
                    } else {
                        return Observable.empty();
                    }
                });
    }
}
