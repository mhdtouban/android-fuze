package io.gmas.fuze.commons.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.StringTokenizer;

import timber.log.Timber;

public class StringUtils {

    public static boolean isEmail(final @NonNull String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public static boolean isEmpty(final @Nullable String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(final @Nullable String str) {
        return !isEmpty(str);
    }

    public static boolean isPresent(final @Nullable String str) {
        return !isEmpty(str);
    }

}
