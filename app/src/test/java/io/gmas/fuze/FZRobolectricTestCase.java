package io.gmas.fuze;

import android.content.Context;
import android.support.annotation.NonNull;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import io.gmas.fuze.commons.services.MockApiUser;
import io.gmas.fuze.commons.session.Session;

@RunWith(FZRobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, shadows = ShadowMultiDex.class, sdk = FZRobolectricGradleTestRunner.DEFAULT_SDK)
public abstract class FZRobolectricTestCase extends TestCase {

    private FZApplicationTest application;
    private Session session;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        session = application().component().session().toBuilder()
                .apiUser(new MockApiUser())
                .build();
    }

    protected @NonNull FZApplicationTest application() {
        if (application != null) {
            return application;
        }

        application = (FZApplicationTest) RuntimeEnvironment.application;
        return application;
    }

    protected @NonNull Context context() {
        return application().getApplicationContext();
    }

    protected @NonNull Session session() {
        return session;
    }

}
