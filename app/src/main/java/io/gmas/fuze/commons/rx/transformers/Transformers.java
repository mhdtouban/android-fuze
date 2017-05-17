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
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/libs/rx/transformers/Transformers.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons.rx.transformers;

import android.support.annotation.NonNull;

import io.gmas.fuze.commons.services.ApiException;
import io.gmas.fuze.commons.services.apiresponses.ErrorEnvelope;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public final class Transformers {
    private Transformers() {
    }

    /**
     * Prevents an observable from erroring by chaining `onErrorResumeNext`.
     */
    public static <T> NeverErrorTransformer<T> neverError() {
        return new NeverErrorTransformer<>();
    }

    /**
     * Emits when an error is thrown in a materialized stream.
     */
    public static @NonNull <T> ErrorsTransformer<T> errors() {
        return new ErrorsTransformer<>();
    }

    /**
     * Prevents an observable from erroring on any {@link ApiException} exceptions.
     */
    public static <T> NeverApiErrorTransformer<T> neverApiError() {
        return new NeverApiErrorTransformer<>();
    }

    /**
     * Prevents an observable from erroring on any {@link ApiException} exceptions,
     * and any errors that do occur will be piped into the supplied
     * errors publish subject. `null` values will never be sent to
     * the publish subject.
     */
    public static <T> NeverApiErrorTransformer<T> pipeApiErrorsTo(final @NonNull PublishSubject<ErrorEnvelope> errorSubject) {
        return new NeverApiErrorTransformer<>(errorSubject::onNext);
    }

    /**
     * Prevents an observable from erroring by chaining `onErrorResumeNext`,
     * and any errors that occur will be piped into the supplied errors publish
     * subject. `null` values will never be sent to the publish subject.
     */
    public static <T> NeverErrorTransformer<T> pipeErrorsTo(final @NonNull PublishSubject<Throwable> errorSubject) {
        return new NeverErrorTransformer<>(errorSubject::onNext);
    }

    /**
     * If called on the main thread, schedule the work immediately. Otherwise delay execution of the work by adding it
     * to a message queue, where it will be executed on the main thread.
     * <p>
     * This is particularly useful for RecyclerViews; if subscriptions in these views are delayed for a frame, then
     * the view temporarily shows recycled content and frame rate stutters. To address that, we can use `observeForUI()`
     * to execute the work immediately rather than wait for a frame.
     */
    public static @NonNull <T> ObserveForUITransformer<T> observeForUI() {
        return new ObserveForUITransformer<>();
    }

    /**
     * Emits the latest value of the source observable whenever the `when`
     * observable emits.
     */
    public static <S, T> TakeWhenTransformer<S, T> takeWhen(final @NonNull Observable<T> when) {
        return new TakeWhenTransformer<>(when);
    }


    /**
     * Emits the latest values from two observables whenever either emits.
     */
    public static <S, T> CombineLatestPairTransformer<S, T> combineLatestPair(final @NonNull Observable<T> second) {
        return new CombineLatestPairTransformer<>(second);
    }
}