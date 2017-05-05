package io.gmas.fuze.commons;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.trello.rxlifecycle2.android.ActivityEvent;

import io.gmas.fuze.commons.ui.datas.ActivityResult;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class ActivityViewModel<ViewType extends ActivityLifecycleType> extends BaseObservable {

    private final PublishSubject<Object> viewChange = PublishSubject.create();
    private final Observable<Object> view = viewChange.filter(v -> v != Irrelevant.CLEAR);
    private final CompositeDisposable subscriptions = new CompositeDisposable();
    private final PublishSubject<ActivityResult> activityResult = PublishSubject.create();
    private final PublishSubject<Intent> intent = PublishSubject.create();

    private enum Irrelevant {CLEAR}

    public ActivityViewModel() {
    }

    public void activityResult(final @NonNull ActivityResult activityResult) {
        this.activityResult.onNext(activityResult);
    }

    public void intent(final @NonNull Intent intent) {
        this.intent.onNext(intent);
    }

    @CallSuper
    protected void onCreate(final @NonNull Context context, final @Nullable Bundle savedInstanceState) {
        Timber.d("onCreate %s", this.toString());
        dropView();
    }

    @CallSuper
    protected void onResume(final @NonNull ViewType view) {
        Timber.d("onResume %s", this.toString());
        onTakeView(view);
    }

    @CallSuper
    protected void onPause() {
        Timber.d("onPause %s", this.toString());
        dropView();
    }

    @CallSuper
    protected void onDestroy() {
        Timber.d("onDestroy %s", this.toString());

        subscriptions.clear();
        viewChange.onComplete();
    }

    private void onTakeView(final @NonNull ViewType view) {
        Timber.d("onTakeView %s %s", this.toString(), view.toString());
        viewChange.onNext(view);
    }

    private void dropView() {
        Timber.d("dropView %s", this.toString());
        viewChange.onNext(Irrelevant.CLEAR);
    }

    protected @NonNull Observable<ActivityResult> activityResult() {
        return activityResult;
    }

    protected @NonNull Observable<Intent> intent() {
        return intent;
    }

    @SuppressWarnings("unchecked")
    public @NonNull <T> ObservableTransformer<T, T> bindToLifecycle() {
        return source -> source.takeUntil(
                view.switchMap(v -> ((ViewType) v).lifecycle().map(e -> Pair.create((ViewType) v, e)))
                        .filter(ve -> isFinished(ve.first, ve.second))
        );
    }

    private boolean isFinished(final @NonNull ViewType view, final @NonNull ActivityEvent event) {
        if (view instanceof BaseActivity) {
            return event == ActivityEvent.DESTROY && ((BaseActivity) view).isFinishing();
        }
        return event == ActivityEvent.DESTROY;
    }

}