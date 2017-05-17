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
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/libs/RecyclerViewPaginator.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class RecyclerViewPaginator {

    private final @NonNull RecyclerView recyclerView;
    private final @NonNull Action fetchNext;
    private Disposable subscription;
    private static final int DIRECTION_DOWN = 1;

    public RecyclerViewPaginator(final @NonNull RecyclerView recyclerView, final @NonNull Action fetchNext) {
        this.recyclerView = recyclerView;
        this.fetchNext = fetchNext;
        start();
    }

    /**
     * Begin listening to the recycler view scroll events to determine
     * when pagination should happen.
     */
    public void start() {
        stop();

        subscription = RxRecyclerView.scrollEvents(recyclerView)
                .map(__ -> recyclerView.getLayoutManager())
                .ofType(LinearLayoutManager.class)
                .map(this::displayedItemFromLinearLayout)
                .filter(__ -> recyclerView.canScrollVertically(DIRECTION_DOWN))
                .filter(item -> item.second != 0)
                .filter(this::visibleItemIsCloseToBottom)
                // NB: We think this operation is suffering from back pressure problems due to the volume of scroll events:
                // https://rink.hockeyapp.net/manage/apps/239008/crash_reasons/88318986
                // If it continues to happen we can also try `debounce`.
                .distinctUntilChanged()
                .subscribe(__ -> fetchNext.run());
    }

    /**
     * Stop listening to recycler view scroll events and discard the
     * associated resources. This should be done when the object that
     * created `this` is released.
     */
    public void stop() {
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
        }
    }

    /**
     * Returns a (visibleItem, totalItemCount) pair given a linear layout manager.
     */
    private @NonNull Pair<Integer, Integer> displayedItemFromLinearLayout(final @NonNull LinearLayoutManager manager) {
        return new Pair<>(manager.findLastVisibleItemPosition(), manager.getItemCount());
    }

    private boolean visibleItemIsCloseToBottom(final @NonNull Pair<Integer, Integer> visibleItemOfTotal) {
        return visibleItemOfTotal.first == visibleItemOfTotal.second - 1;
    }

}
