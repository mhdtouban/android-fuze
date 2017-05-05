package io.gmas.fuze.commons.utils;

import android.os.Looper;

public class ThreadUtils {
    private ThreadUtils() {}

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
