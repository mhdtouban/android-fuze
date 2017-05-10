package io.gmas.fuze.commons.services;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Map;

import io.gmas.fuze.commons.services.apiresponses.CommentEnvelope;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MockApiUser implements ApiUserType {
    private final PublishSubject<Pair<String, Map<String, Object>>> observable = PublishSubject.create();

    public @NonNull Observable<Pair<String, Map<String, Object>>> observable() {
        return observable;
    }

    @Override public @NonNull Observable<CommentEnvelope> fetchComments() {
        return null;
    }
}
