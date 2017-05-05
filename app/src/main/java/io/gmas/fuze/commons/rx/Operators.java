package io.gmas.fuze.commons.rx;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class Operators {
    private Operators() {
    }

    public static @NonNull <T> ApiErrorOperator<T> apiError(Gson gson) {
        return new ApiErrorOperator<>(gson);
    }
}
