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
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/libs/rx/transformers/NeverApiErrorTransformer.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

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
