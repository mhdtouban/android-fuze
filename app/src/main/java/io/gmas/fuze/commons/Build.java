package io.gmas.fuze.commons;

import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;

import java.util.Locale;

import io.gmas.fuze.BuildConfig;

public class Build {

    private final PackageInfo packageInfo;

    public Build(final @NonNull PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public
    @NonNull
    String applicationId() {
        return packageInfo.packageName;
    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public boolean isRelease() {
        return !BuildConfig.DEBUG;
    }

    public Integer versionCode() {
        return packageInfo.versionCode;
    }

    public String versionName() {
        return packageInfo.versionName;
    }

    public String variant() {
        return BuildConfig.FLAVOR +
                BuildConfig.BUILD_TYPE.substring(0, 1).toUpperCase(Locale.US) +
                BuildConfig.BUILD_TYPE.substring(1);
    }

}
