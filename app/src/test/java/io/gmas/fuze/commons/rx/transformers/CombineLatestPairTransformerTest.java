package io.gmas.fuze.commons.rx.transformers;

import android.util.Pair;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;

public final class CombineLatestPairTransformerTest {

    @Test
    public void testCombineLatestPair() {
        final PublishSubject<Integer> first = PublishSubject.create();
        final PublishSubject<Integer> second = PublishSubject.create();
        final Observable<Pair<Integer, Integer>> result = first.compose(Transformers.combineLatestPair(second));

        final TestSubscriber<Pair<Integer, Integer>> resultTest = new TestSubscriber<>();
        result.subscribe(resultTest::onNext);

        first.onNext(1);
        resultTest.assertNoValues();
    }

}
