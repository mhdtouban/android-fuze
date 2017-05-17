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
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/libs/rx/operators/ApiErrorOperator.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons.rx.operators;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import io.gmas.fuze.commons.services.ApiException;
import io.gmas.fuze.commons.services.ResponseException;
import io.gmas.fuze.commons.services.apiresponses.ErrorEnvelope;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ApiErrorOperator<T> implements ObservableOperator<T, Response<T>> {

    private final Gson gson;

    public ApiErrorOperator(final @NonNull Gson gson) {
        this.gson = gson;
    }

    @Override public Observer<? super Response<T>> apply(Observer<? super T> observer) throws Exception {
        return new Observer<Response<T>>() {

            @Override public void onSubscribe(Disposable d) {
                observer.onSubscribe(d);
            }

            @Override public void onNext(Response<T> response) {
                if (!response.isSuccessful()) {
                    try {
                        String jsonError = response.errorBody().string();
                        final ErrorEnvelope envelope = gson.fromJson(jsonError,
                                ErrorEnvelope.class);
                        observer.onError(new ApiException(envelope, response));
                    } catch (final @NonNull IOException e) {
                        observer.onError(new ResponseException(response));
                    }
                }
                observer.onNext(response.body());
            }

            @Override public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override public void onComplete() {
                observer.onComplete();
            }
        };
    }
}
