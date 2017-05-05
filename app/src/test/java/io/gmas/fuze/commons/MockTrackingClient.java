package io.gmas.fuze.commons;

import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MockTrackingClient extends TrackingClientType {

    public static class Event {
        private final String name;
        private final Map<String, Object> properties;
        public Event(final String name, final Map<String, Object> properties) {
            this.name = name;
            this.properties = properties;
        }
    }

    public final @NonNull PublishSubject<Event> events = PublishSubject.create();
    public final @NonNull Observable<String> eventNames = events.map(e -> e.name);

    @Override public void trackGoogleAnalytics(@NonNull String screenName) {

    }
}
