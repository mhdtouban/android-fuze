package io.gmas.fuze.commons.rx.transformers;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;

public final class TakeWhenTransformerTest {

    @Test
    public void testTakeWhen_sourceEmitsFirst() {

        final PublishSubject<Integer> source = PublishSubject.create();
        final PublishSubject<Boolean> sample = PublishSubject.create();
        final Observable<Integer> result = source.compose(Transformers.takeWhen(sample));

        final TestSubscriber<Integer> resultTest = new TestSubscriber<>();
        result.subscribe(resultTest::onNext);

        source.onNext(1);
        resultTest.assertNoValues();

        source.onNext(2);
        resultTest.assertNoValues();

        sample.onNext(true);
        resultTest.assertValues(2);

        sample.onNext(true);
        resultTest.assertValues(2, 2);

        source.onNext(3);
        resultTest.assertValues(2, 2);

        sample.onNext(true);
        resultTest.assertValues(2, 2, 3);
    }

    @Test
    public void testTakeWhen_sourceEmitsSecond() {

        final PublishSubject<Integer> source = PublishSubject.create();
        final PublishSubject<Boolean> sample = PublishSubject.create();
        final Observable<Integer> result = source.compose(Transformers.takeWhen(sample));

        final TestSubscriber<Integer> resultTest = new TestSubscriber<>();
        result.subscribe(resultTest::onNext);

        sample.onNext(true);
        resultTest.assertNoValues();

        sample.onNext(true);
        resultTest.assertNoValues();

        source.onNext(1);
        resultTest.assertNoValues();

        sample.onNext(true);
        resultTest.assertValues(1);

        source.onNext(2);
        resultTest.assertValues(1);

        sample.onNext(true);
        resultTest.assertValues(1, 2);

        source.onNext(3);
        resultTest.assertValues(1, 2);

        sample.onNext(true);
        resultTest.assertValues(1, 2, 3);
    }
}
