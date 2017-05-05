package io.gmas.fuze.commons.rx.transformers;

import android.os.Looper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowLooper;

import java.util.concurrent.atomic.AtomicInteger;

import io.gmas.fuze.FZRobolectricTestCase;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static io.gmas.fuze.commons.rx.transformers.Transformers.observeForUI;

public class ObserveForUITransformerTest extends FZRobolectricTestCase {
    @Before
    public void setUp() {
        RxAndroidPlugins.reset();
        ShadowLooper.pauseMainLooper();
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.reset();
        ShadowLooper.unPauseMainLooper();
    }

    @Test
    public void test() {
        final Scheduler scheduler = AndroidSchedulers.from(Looper.getMainLooper());
        final AtomicInteger x = new AtomicInteger();

        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x::set);

        // Main looper is paused, so value should not change.
        assertEquals(0, x.get());

        Observable.just(2)
                .observeOn(Schedulers.trampoline())
                .subscribe(x::set);

        // Since the work used the immediate scheduler, it is unaffected by the main looper being paused.
        assertEquals(2, x.get());

        Observable.just(3)
                .compose(observeForUI())
                .subscribe(x::set);

        // The main looper is paused but the code is executing on the main thread, so observeForUI() should schedule the
        // work immediately rather than queueing it up.
        assertEquals(3, x.get());

        // Run the queued work.
        ShadowLooper.runUiThreadTasks();

        // Code observed using `AndroidSchedulers.mainThread()` is now run.
        assertEquals(1, x.get());
    }
}
