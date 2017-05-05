package io.gmas.fuze.commons.utils;

import android.content.res.Resources;
import android.support.annotation.NonNull;

public class UiUtils {

    public static int getStatusBarHeight(final @NonNull Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
