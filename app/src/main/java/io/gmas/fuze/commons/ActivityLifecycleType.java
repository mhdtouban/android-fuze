package io.gmas.fuze.commons;

import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;

public interface ActivityLifecycleType {

    Observable<ActivityEvent> lifecycle();

}
