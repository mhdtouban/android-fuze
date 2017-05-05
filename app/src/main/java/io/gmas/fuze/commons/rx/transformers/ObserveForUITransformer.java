package io.gmas.fuze.commons.rx.transformers;

import io.gmas.fuze.commons.utils.ThreadUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class ObserveForUITransformer<T> implements ObservableTransformer<T, T> {

    @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.flatMap(value -> {
            if (ThreadUtils.isMainThread()) {
                return Observable.just(value).observeOn(Schedulers.trampoline());
            } else {
                return Observable.just(value).observeOn(AndroidSchedulers.mainThread());
            }
        });
    }
}