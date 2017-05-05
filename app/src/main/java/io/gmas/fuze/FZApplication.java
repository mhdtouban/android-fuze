package io.gmas.fuze;

import android.support.annotation.CallSuper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;

public class FZApplication extends MultiDexApplication {

    private FZApplicationComponent component;

    @Override
    @CallSuper
    public void onCreate() {
        super.onCreate();

        component = DaggerFZApplicationComponent.builder()
                .fZApplicationModule(new FZApplicationModule(this))
                .build();
        component().inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        MultiDex.install(this);
        JodaTimeAndroid.init(this);
        Fresco.initialize(this);

        if (!isInUnitTests()) {
            LeakCanary.install(this);
        }

    }

    public FZApplicationComponent component() {
        return component;
    }

    public boolean isInUnitTests() {
        return false;
    }

}
