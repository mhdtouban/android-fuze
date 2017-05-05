package io.gmas.fuze.commons.services;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import io.gmas.fuze.commons.rx.ApiErrorOperator;
import io.gmas.fuze.commons.rx.Operators;

public class ApiUser implements ApiUserType {

    private ApiUserService service;
    private Gson gson;

    public ApiUser(final @NonNull ApiUserService service, final @NonNull Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    private @NonNull <T> ApiErrorOperator<T> apiErrorOperator() {
        return Operators.apiError(gson);
    }

}