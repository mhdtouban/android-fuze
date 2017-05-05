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
