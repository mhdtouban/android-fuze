package io.gmas.fuze.commons.services;

import io.gmas.fuze.commons.services.apiresponses.CommentEnvelope;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiUserService {

    @GET("/comments")
    Observable<Response<CommentEnvelope>> fetchComments();

}
