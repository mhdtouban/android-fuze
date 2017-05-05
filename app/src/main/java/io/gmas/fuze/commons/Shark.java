package io.gmas.fuze.commons;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Shark {

    private final @NonNull TrackingClientType client;

    public Shark(final @NonNull TrackingClientType client) {
        this.client = client;
    }

    public @NonNull TrackingClientType client() {
        return client;
    }

    // ===> Tracking Google Analytics <===

    public void trackView(final @NonNull String view) {
        client.trackGoogleAnalytics(view);
    }

}
