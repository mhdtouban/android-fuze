package io.gmas.fuze.commons;

import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class FragmentViewModel<ViewType extends FragmentLifecycleType> extends BaseObservable {

    private final PublishSubject<Object> viewChange = PublishSubject.create();
    private final Observable<Object> view = viewChange.filter(v -> v != Irrelevant.CLEAR);
    private final PublishSubject<Bundle> arguments = PublishSubject.create();

    private enum Irrelevant {CLEAR}

    public FragmentViewModel() {
    }

    @CallSuper
    protected void onCreate(final @NonNull Context context, final @Nullable Bundle savedInstanceState) {
        Timber.d("onCreate %s", this.toString());
        dropView();
    }

    /**
     * Takes bundle arguments from the view.
     */
    public void arguments(final @Nullable Bundle bundle) {
        this.arguments.onNext(bundle);
    }

    protected @NonNull Observable<Bundle> arguments() {
        return arguments;
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
        dropView();
    }

    @CallSuper
    protected void onDetach() {
        Timber.d("onDetach %s", this.toString());
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

    protected final @NonNull Observable<Object> view() {
        return view;
    }

    /**
     * By composing this transformer with an observable you guarantee that every observable in your view model
     * will be properly completed when the view model completes.
     * <p>
     * It is required that *every* observable in a view model do `.compose(bindToLifecycle())` before calling
     * `subscribe`.
     */
    @SuppressWarnings("unchecked")
    public @NonNull <T> ObservableTransformer<T, T> bindToLifecycle() {
        return source -> source.takeUntil(
                view.switchMap(v -> ((ViewType) v).lifecycle().map(e -> Pair.create((ViewType) v, e)))
                        .filter(ve -> isFinished(ve.first, ve.second))
        );
    }

    private boolean isFinished(final @NonNull ViewType view, final @NonNull FragmentEvent event) {
        if (view instanceof BaseFragment) {
            return event == FragmentEvent.DETACH && ((BaseFragment) view).isDetached();
        }
        return event == FragmentEvent.DETACH;
    }
}
