package io.gmas.fuze.commons.rx.transformers;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;

public final class ErrorsTransformerTest {
    @Test
    public void testEmitsErrors() {
        final PublishSubject<Integer> source = PublishSubject.create();
        final Observable<Throwable> result = source
                .materialize()
                .compose(Transformers.errors());

        final TestSubscriber<Throwable> resultTest = new TestSubscriber<>();
        result.subscribe(resultTest::onNext);

        source.onNext(1);
        resultTest.assertNoValues();

        // Only emit when an error is thrown.
        source.onError(new Throwable());
        resultTest.assertValueCount(1);
    }

    @Test
    public void testCompletedDoesNotEmitAnErrorNotification() {
        final PublishSubject<Integer> source = PublishSubject.create();
        final Observable<Throwable> result = source
                .materialize()
                .compose(Transformers.errors());

        final TestSubscriber<Throwable> resultTest = new TestSubscriber<>();
        result.subscribe(resultTest::onNext);

        source.onComplete();
        resultTest.assertValueCount(0);
    }
}
