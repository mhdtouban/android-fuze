package io.gmas.fuze.commons.rx.transformers;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public final class ErrorsTransformer<T> implements ObservableTransformer<Notification<T>, Throwable> {

    @Override public ObservableSource<Throwable> apply(Observable<Notification<T>> upstream) {
        return upstream.filter(Notification::isOnError)
                .map(Notification::getError);
    }
}
