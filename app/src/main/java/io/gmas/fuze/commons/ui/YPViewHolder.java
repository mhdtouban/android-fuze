/*
 * Copyright 2017 Kickstarter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ***
 *
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/ui/viewholders/KSViewHolder.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.gmas.fuze.FZApplication;
import io.gmas.fuze.commons.ActivityLifecycleType;
import io.gmas.fuze.commons.session.Session;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public abstract class YPViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        ActivityLifecycleType {

    private final View view;
    private final @NonNull PublishSubject<ActivityEvent> lifecycle = PublishSubject.create();

    public YPViewHolder(final @NonNull View view) {
        super(view);
        this.view = view;
    }

    /**
     * No-op click implementation. Subclasses should override this method to implement click handling.
     */
    @Override public void onClick(final @NonNull View view) {
        Timber.d("Default YPViewHolder onClick event");
    }

    /**
     * Populate a view with data that was bound in `bindData`.
     *
     * Prefer creating subscriptions to a viewmodel in the constructor, then using #{link #bindData} to
     *             send new data to the viewmodel.
     */
    public void onBind() {}

    /**
     * Implementations of this should inspect `data` to set instance variables in the view holder that
     * `onBind` can then use without worrying about type safety.
     *
     * @throws Exception Raised when binding is unsuccessful.
     */
    abstract public void bindData(final @Nullable Object data) throws Exception;

    @Override
    public @NonNull Observable<ActivityEvent> lifecycle() {
        return lifecycle;
    }

    /**
     * This method is intended to be called only from `YPAdapter` in order for it to inform the view holder
     * of its lifecycle.
     */
    public void lifecycleEvent(final @NonNull ActivityEvent event) {
        lifecycle.onNext(event);

        if (ActivityEvent.DESTROY.equals(event)) {
            destroy();
        }
    }

    /**
     * Completes an observable when an {@link ActivityEvent} occurs in the activity's lifecycle.
     */
    public final @NonNull <T> ObservableTransformer<T, T> bindUntilEvent(final @NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycle, event);
    }

    /**
     * Completes an observable when the lifecycle event opposing the current lifecyle event is emitted.
     * For example, if a subscription is made during {@link ActivityEvent#CREATE}, the observable will be completed
     * in {@link ActivityEvent#DESTROY}.
     */
    public final @NonNull <T> ObservableTransformer<T, T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycle);
    }

    /**
     * Called when the ViewHolder is being detached. Subclasses should override if they need to do any work
     * when the ViewHolder is being de-allocated.
     */
    protected void destroy() {}

    protected @NonNull View view() {
        return view;
    }

    protected @NonNull Context context() {
        return view.getContext();
    }

    protected @NonNull Session session() {
        return ((FZApplication) context().getApplicationContext()).component().session();
    }

}
