package io.gmas.fuze.commons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.gmas.fuze.commons.models.User;
import io.gmas.fuze.commons.qualifiers.ApplicationContext;

public class SharkTrackingClient extends TrackingClientType {

    @Inject CurrentUserType currentUser;
    private @Nullable User loggedInUser;

//    private final @NonNull Tracker googleAnalyticsTracker;

    public SharkTrackingClient(final @ApplicationContext @NonNull Context context,
                               final @NonNull CurrentUserType currentUser) {

        this.currentUser = currentUser;
        this.currentUser.observable().subscribe(u -> loggedInUser = u);
//        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(context);
//        this.googleAnalyticsTracker = googleAnalytics.newTracker(R.xml.global_tracker);
    }

    @Override public void trackGoogleAnalytics(final @NonNull String screenName) {
//        googleAnalyticsTracker.setScreenName(screenName);
//        googleAnalyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
