package io.gmas.fuze.commons.services;

import android.support.annotation.NonNull;

import io.gmas.fuze.commons.services.apiresponses.CommentEnvelope;
import io.reactivex.Observable;

public interface ApiUserType {

    @NonNull Observable<CommentEnvelope> fetchComments();

}
