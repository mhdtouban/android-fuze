package io.gmas.fuze.commons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.gmas.fuze.FZApplication;
import io.gmas.fuze.FZApplicationComponent;
import io.gmas.fuze.commons.qualifiers.RequiresActivityViewModel;
import io.gmas.fuze.commons.session.Session;
import io.gmas.fuze.commons.ui.datas.ActivityResult;
import io.gmas.fuze.commons.utils.BundleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;


public class BaseActivity<ViewModelType extends ActivityViewModel> extends RxAppCompatActivity
        implements ActivityLifecycleType {

    private final PublishSubject<Boolean> back = PublishSubject.create();
    private final BehaviorSubject<ActivityEvent> lifecycle = BehaviorSubject.create();
    private static final String VIEW_MODEL_KEY = "viewModel";
    private final CompositeDisposable subscriptions = new CompositeDisposable();
    protected ViewModelType viewModel;

    /**
     * Get viewModel.
     */
    public ViewModelType viewModel() {
        return viewModel;
    }

    /**
     * Sends activity result data to the view model.
     */
    @CallSuper
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        viewModel.activityResult(ActivityResult.create(requestCode, resultCode, intent));
    }

    @CallSuper
    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate %s", this.toString());

        lifecycle.onNext(ActivityEvent.CREATE);

        assignViewModel(savedInstanceState);

        viewModel.intent(getIntent());
    }

    /**
     * Called when an activity is set to `singleTop` and it is relaunched while at the top of the activity stack.
     */
    @CallSuper
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        viewModel.intent(intent);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart %s", this.toString());
        lifecycle.onNext(ActivityEvent.START);

        back
                .compose(bindUntilEvent(ActivityEvent.STOP))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> goBack());
    }

    @SuppressWarnings("unchecked")
    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume %s", this.toString());
        lifecycle.onNext(ActivityEvent.RESUME);

        assignViewModel(null);
        if (viewModel != null) {
            viewModel.onResume(this);
        }
    }

    @CallSuper
    @Override
    protected void onPause() {
        lifecycle.onNext(ActivityEvent.PAUSE);
        super.onPause();
        Timber.d("onPause %s", this.toString());

        if (viewModel != null) {
            viewModel.onPause();
        }
    }

    @CallSuper
    @Override
    protected void onStop() {
        lifecycle.onNext(ActivityEvent.STOP);
        super.onStop();
        Timber.d("onStop %s", this.toString());
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        lifecycle.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        Timber.d("onDestroy %s", this.toString());

        subscriptions.clear();

        if (isFinishing()) {
            if (viewModel != null) {
                ActivityViewModelManager.getInstance().destroy(viewModel);
                viewModel = null;
            }
        }
    }

    /**
     * @deprecated Use {@link #back()} instead.
     * <p>
     * In rare situations, onBackPressed can be triggered after {@link #onSaveInstanceState(Bundle)} has been called.
     * This causes an {@link IllegalStateException} in the fragment manager's `checkStateLoss` method, because the
     * UI state has changed after being saved. The sequence of events might look likePhoto this:
     * <p>
     * onSaveInstanceState -> onStop -> onBackPressed
     * <p>
     * To avoid that situation, we need to ignore calls to `onBackPressed` after the activity has been saved. Since
     * the activity is stopped after `onSaveInstanceState` is called, we can create an observable of back events,
     * and a subscription that calls super.onBackPressed() only when the activity has not been stopped.
     */
    @CallSuper
    @Override
    @Deprecated
    public void onBackPressed() {
        back();
    }

    /**
     * Call when the user wants triggers a back event, e.g. clicking back in a toolbar or pressing the device back button.
     */
    public void back() {
        back.onNext(true);
    }

    /**
     * Override in subclasses for custom exit transitions. First item in pair is the enter animation,
     * second item in pair is the exit animation.
     */
    protected @Nullable Pair<Integer, Integer> exitTransition() {
        return null;
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("onSaveInstanceState %s", this.toString());

        final Bundle viewModelEnvelope = new Bundle();
        if (viewModel != null) {
            ActivityViewModelManager.getInstance().save(viewModel, viewModelEnvelope);
        }

        outState.putBundle(VIEW_MODEL_KEY, viewModelEnvelope);
    }

    protected final void startActivityWithTransition(final @NonNull Intent intent, final @AnimRes int enterAnim,
                                                     final @AnimRes int exitAnim) {
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * Returns the {@link FZApplication} instance.
     */
    protected @NonNull FZApplication application() {
        return (FZApplication) getApplication();
    }

    /**
     * Convenience method to return a Dagger component.
     */
    protected @NonNull FZApplicationComponent component() {
        return application().component();
    }

    /**
     * Returns the application's {@link Session}.
     */
    protected @NonNull Session session() {
        return component().session();
    }

    /**
     * Triggers a back press with an optional transition.
     */
    private void goBack() {
        super.onBackPressed();

        final Pair<Integer, Integer> exitTransitions = exitTransition();
        if (exitTransitions != null) {
            overridePendingTransition(exitTransitions.first, exitTransitions.second);
        }
    }

    @SuppressWarnings("unchecked")
    private void assignViewModel(final @Nullable Bundle viewModelEnvelope) {
        if (viewModel == null) {
            final RequiresActivityViewModel annotation = getClass().getAnnotation(RequiresActivityViewModel.class);
            final Class<ViewModelType> viewModelClass = annotation == null ? null : (Class<ViewModelType>) annotation.value();
            if (viewModelClass != null) {
                viewModel = ActivityViewModelManager.getInstance().fetch(this,
                        viewModelClass,
                        BundleUtils.maybeGetBundle(viewModelEnvelope, VIEW_MODEL_KEY));
            }
        }
    }
}
