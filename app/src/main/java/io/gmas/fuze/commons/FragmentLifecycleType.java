package io.gmas.fuze.commons;

import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;

public interface FragmentLifecycleType {

    Observable<FragmentEvent> lifecycle();

}
