package io.gmas.fuze.commons.rx.transformers;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;

public final class NeverErrorTransformerTest {
    @Test
    public void testNeverError_emitsSameValuesAsSource() {

        final Observable<Integer> source = Observable.just(1, 2, 3, 4);
        final Observable<Integer> result = source.compose(Transformers.neverError());

        final TestSubscriber<Integer> resultTest = TestSubscriber.create();
        result.subscribe(resultTest::onNext);

        resultTest.assertValues(1, 2, 3, 4);
        resultTest.assertNotComplete();
    }

    @Test
    public void testNeverError_emitsSameValuesAndSkipsError() {

        final Observable<Integer> errorsOnLast = Observable.just(1, 2, 3, 4)
                .flatMap(i -> i < 4 ? Observable.just(i) : Observable.error(new RuntimeException()));
        final Observable<Integer> result = errorsOnLast.compose(Transformers.neverError());

        final TestSubscriber<Integer> resultTest = TestSubscriber.create();
        result.subscribe(resultTest::onNext);

        resultTest.assertValues(1, 2, 3);
        resultTest.assertNotComplete();
    }

    @Test
    public void testNeverError_pipesErrorsToPublishSubject() {

        final RuntimeException exception = new RuntimeException();
        final Observable<Integer> errorsOnLast = Observable.just(1, 2, 3, 4)
                .flatMap(i -> i < 4 ? Observable.just(i) : Observable.error(exception));
        final PublishSubject<Throwable> error = PublishSubject.create();
        final Observable<Integer> result = errorsOnLast.compose(Transformers.pipeErrorsTo(error));

        final TestSubscriber<Throwable> errorTest = TestSubscriber.create();
        error.subscribe(errorTest::onNext);
        final TestSubscriber<Integer> resultTest = TestSubscriber.create();
        result.subscribe(resultTest::onNext);

        resultTest.assertValues(1, 2, 3);
        resultTest.assertNotComplete();

        errorTest.assertValues(exception);
        errorTest.assertNotComplete();
    }
}
