package io.gmas.fuze.commons.factories;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import io.gmas.fuze.commons.models.User;
import io.gmas.fuze.commons.services.ApiException;
import io.gmas.fuze.commons.services.apiresponses.ErrorEnvelope;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public final class ApiExceptionFactory {
    private ApiExceptionFactory() {}

    public static @NonNull ApiException badRequestException() {
        final ErrorEnvelope envelope = ErrorEnvelope.builder()
                .error("bad request")
                .code(400)
                .build();
        final ResponseBody body = ResponseBody.create(null, "");
        final Response<Observable<User>> response = Response.error(400, body);

        return new ApiException(envelope, response);
    }

    public static @NonNull ApiException apiError(final @NonNull ErrorEnvelope errorEnvelope) {
        final ResponseBody body = ResponseBody.create(null, new Gson().toJson(errorEnvelope));
        final Response<Observable<User>> response = Response.error(errorEnvelope.code(), body);

        return new ApiException(errorEnvelope, response);
    }

    public static @NonNull ApiException invalidLoginException() {
        final ErrorEnvelope envelope = ErrorEnvelope.builder()
                .error("Invalid login.")
                .code(401)
                .build();

        final ResponseBody body = ResponseBody.create(null, new Gson().toJson(envelope));
        final Response<Observable<User>> response = Response.error(envelope.code(), body);

        return new ApiException(envelope, response);
    }

}