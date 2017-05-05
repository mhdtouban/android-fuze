package io.gmas.fuze.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import io.gmas.fuze.R;

public class TransitionUtils {

    private TransitionUtils() {}

    /**
     * Explicitly set a transition after starting an activity.
     *
     * @param context The activity that started the new intent.
     * @param transition A pair of animation ids, first is the enter animation, second is the exit animation.
     */
    public static void transition(final @NonNull Context context, final @NonNull Pair<Integer, Integer> transition) {
        if (!(context instanceof Activity)) {
            return;
        }

        final Activity activity = (Activity) context;
        activity.overridePendingTransition(transition.first, transition.second);
    }

    public static @NonNull Pair<Integer, Integer> enter() {
        return Pair.create(R.anim.slide_in_right, R.anim.zoom_out);
    }

    public static @NonNull Pair<Integer, Integer> exit() {
        return Pair.create(R.anim.zoom_in, R.anim.slide_out_right);
    }

    public static @NonNull Pair<Integer, Integer> fadeIn() {
        return Pair.create(R.anim.fade_in, R.anim.stay);
    }

    public static @NonNull Pair<Integer, Integer> slideInUp() {
        return Pair.create(R.anim.slide_in_up, R.anim.stay);
    }

    public static @NonNull Pair<Integer, Integer> slideOutDown() {
        return Pair.create(R.anim.stay, R.anim.slide_out_down);
    }

}
