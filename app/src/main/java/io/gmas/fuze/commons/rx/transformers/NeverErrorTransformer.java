package io.gmas.fuze.commons.rx.transformers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;

public final class NeverErrorTransformer<T> implements ObservableTransformer<T, T> {

    private final @Nullable Consumer<Throwable> errorAction;

    protected NeverErrorTransformer() {
        this.errorAction = null;
    }

    protected NeverErrorTransformer(final @Nullable Consumer<Throwable> errorAction) {
        this.errorAction = errorAction;
    }

    @Override @NonNull public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnError(e -> {
                    if (errorAction != null) {
                        errorAction.accept(e);
                    }
                })
                .onErrorResumeNext(Observable.empty());
    }
}