package io.gmas.fuze.commons.rx.operators;

import com.google.gson.Gson;

import org.junit.Test;

import io.gmas.fuze.FZRobolectricTestCase;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.ResponseBody;
import retrofit2.Response;

public final class ApiErrorOperatorTest extends FZRobolectricTestCase {

    @Test
    public void testErrorResponse() {
        final Gson gson = new Gson();

        final PublishSubject<Response<Integer>> response = PublishSubject.create();
        final Observable<Integer> result = response.lift(Operators.apiError(gson));

        final TestSubscriber<Integer> resultTest = new TestSubscriber<>();
        result.subscribe(resultTest::onNext, e -> {});

        response.onNext(Response.error(400, ResponseBody.create(null, "")));

        resultTest.assertNoValues();
    }

    @Test
    public void testSuccessResponse() {
        final Gson gson = new Gson();

        final PublishSubject<Response<Integer>> response = PublishSubject.create();
        final Observable<Integer> result = response.lift(Operators.apiError(gson));

        final TestSubscriber<Integer> resultTest = new TestSubscriber<>();
        result.subscribe(resultTest::onNext);

        response.onNext(Response.success(42));

        resultTest.assertValues(42);
    }

}
