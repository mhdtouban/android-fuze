package io.gmas.fuze.commons;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import io.gmas.fuze.commons.utils.ListUtils;
import io.gmas.fuze.commons.utils.ObjectUtils;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static io.gmas.fuze.commons.rx.transformers.Transformers.neverError;
import static io.gmas.fuze.commons.rx.transformers.Transformers.takeWhen;

public final class Paginator<Data, Envelope, Params> {

    private final @NonNull Observable<Boolean> fetchNext;
    private final @NonNull Observable<Params> startOverWith;
    private final @NonNull Function<Envelope, List<Data>> envelopeToListOfData;
    private final @NonNull Function<Envelope, Integer> envelopeToMoreOffset;
    private final @NonNull Function<Params, Observable<Envelope>> loadWithParams;
    private final @NonNull BiFunction<Params, Integer, Observable<Envelope>> loadWithPaginationOffset;
    private final @NonNull BiFunction<List<Data>, List<Data>, List<Data>> concater;
    private final boolean clearWhenStartingOver;
    private final boolean distinctUntilChanged;
    private final boolean hasTimestampOffset;
    private final @NonNull Integer limit;

    private final @NonNull BehaviorSubject<Integer> _moreOffset = BehaviorSubject.create();
    private final @NonNull PublishSubject<Boolean> _isFetching = PublishSubject.create();

    public @NonNull Observable<List<Data>> paginatedData() {
        return paginatedData;
    }

    private final @NonNull Observable<List<Data>> paginatedData;

    public @NonNull Observable<Boolean> isFetching() {
        return isFetching;
    }

    private final @NonNull Observable<Boolean> isFetching = _isFetching;

    private Paginator(final @NonNull Observable<Boolean> fetchNext,
                      final @NonNull Observable<Params> startOverWith,
                      final @NonNull Function<Envelope, List<Data>> envelopeToListOfData,
                      final @NonNull Function<Envelope, Integer> envelopeToMoreOffset,
                      final @NonNull Function<Params, Observable<Envelope>> loadWithParams,
                      final @NonNull BiFunction<Params, Integer, Observable<Envelope>> loadWithPaginationOffset,
                      final boolean clearWhenStartingOver,
                      final @NonNull BiFunction<List<Data>, List<Data>, List<Data>> concater,
                      final boolean distinctUntilChanged,
                      final boolean hasTimestampOffset,
                      final @NonNull Integer limit) {

        this.fetchNext = fetchNext;
        this.startOverWith = startOverWith;
        this.envelopeToListOfData = envelopeToListOfData;
        this.envelopeToMoreOffset = envelopeToMoreOffset;
        this.loadWithParams = loadWithParams;
        this.loadWithPaginationOffset = loadWithPaginationOffset;
        this.clearWhenStartingOver = clearWhenStartingOver;
        this.concater = concater;
        this.distinctUntilChanged = distinctUntilChanged;
        this.hasTimestampOffset = hasTimestampOffset;
        this.limit = limit;

        this.paginatedData = this.startOverWith.switchMap(this::dataWithPagination);
    }

    public final static class Builder<Data, Envelope, Params> {
        private Observable<Boolean> fetchNext;
        private Observable<Params> startOverWith;
        private Function<Envelope, List<Data>> envelopeToListOfData;
        private Function<Envelope, Integer> envelopeToMoreOffset;
        private Function<Params, Observable<Envelope>> loadWithParams;
        private BiFunction<Params, Integer, Observable<Envelope>> loadWithPaginationOffset;
        private boolean clearWhenStartingOver;
        private BiFunction<List<Data>, List<Data>, List<Data>> concater = ListUtils::concat;
        private boolean distinctUntilChanged;
        private boolean hasTimestampOffset;
        private Integer limit;

        /**
         * [Required] An observable that emits whenever a new page of data should be loaded.
         */
        public @NonNull Builder<Data, Envelope, Params> fetchNext(final @NonNull Observable<Boolean> fetchNext) {
            this.fetchNext = fetchNext;
            return this;
        }

        /**
         * [Optional] An observable that emits when a fresh first page should be loaded.
         */
        public @NonNull Builder<Data, Envelope, Params> startOverWith(final @NonNull Observable<Params> startOverWith) {
            this.startOverWith = startOverWith;
            return this;
        }

        /**
         * [Required] A function that takes an `Envelope` instance and returns the list of data embedded in it.
         */
        public @NonNull Builder<Data, Envelope, Params> envelopeToListOfData(final @NonNull Function<Envelope, List<Data>> envelopeToListOfData) {
            this.envelopeToListOfData = envelopeToListOfData;
            return this;
        }

        /**
         * [Optional] A function to extract the more offset from an API response envelope.
         */
        public @NonNull Builder<Data, Envelope, Params> envelopeToMoreOffset(final @NonNull Function<Envelope, Integer> envelopeToMoreOffset) {
            this.envelopeToMoreOffset = envelopeToMoreOffset;
            return this;
        }

        /**
         * [Required] A function that takes a `Params` and performs the associated network request
         * and returns an `Observable<Envelope>`
         */
        public @NonNull Builder<Data, Envelope, Params> loadWithParams(final @NonNull Function<Params, Observable<Envelope>> loadWithParams) {
            this.loadWithParams = loadWithParams;
            return this;
        }

        /**
         * [Optional] Determines if the list of loaded data is cleared when starting over from the first page.
         */
        public @NonNull Builder<Data, Envelope, Params> clearWhenStartingOver(final boolean clearWhenStartingOver) {
            this.clearWhenStartingOver = clearWhenStartingOver;
            return this;
        }

        /**
         * [Required] A function that makes an API request with a pagination URL.
         */
        public @NonNull Builder<Data, Envelope, Params> loadWithPaginationOffset(final @NonNull BiFunction<Params, Integer, Observable<Envelope>> loadWithPaginationOffset) {
            this.loadWithPaginationOffset = loadWithPaginationOffset;
            return this;
        }

        /**
         * [Optional] Determines how two lists are concatenated together while paginating. A regular `ListUtils::concat` is probably
         * sufficient, but sometimes you may want `ListUtils::concatDistinct`
         */
        public @NonNull Builder<Data, Envelope, Params> concater(final @NonNull BiFunction<List<Data>, List<Data>, List<Data>> concater) {
            this.concater = concater;
            return this;
        }

        /**
         * [Optional] Determines if the list of loaded data is should be distinct until changed.
         */
        public @NonNull Builder<Data, Envelope, Params> distinctUntilChanged(final boolean distinctUntilChanged) {
            this.distinctUntilChanged = distinctUntilChanged;
            return this;
        }

        /**
         * [Optional] Determines if the list of loaded data is should be distinct until changed.
         */
        public @NonNull Builder<Data, Envelope, Params> hasTimestampOffset(final boolean hasTimestampOffset) {
            this.hasTimestampOffset = hasTimestampOffset;
            return this;
        }

        /**
         * [Optional] Determines limit.
         */
        public @NonNull Builder<Data, Envelope, Params> limit(final Integer limit) {
            this.limit = limit;
            return this;
        }

        public @NonNull Paginator<Data, Envelope, Params> build() throws RuntimeException {
            // Early error when required field is not set
            if (fetchNext == null) {
                throw new RuntimeException("`fetchNext` is required");
            }
            if (envelopeToListOfData == null) {
                throw new RuntimeException("`envelopeToListOfData` is required");
            }
            if (loadWithParams == null) {
                throw new RuntimeException("`loadWithParams` is required");
            }
            if (loadWithPaginationOffset == null) {
                throw new RuntimeException("`loadWithPaginationPath` is required");
            }

            // Default params for optional fields
            if (startOverWith == null) {
                startOverWith = Observable.empty();
            }
            if (concater == null) {
                concater = ListUtils::concat;
            }
            if (limit == null) {
                limit = 20;
            }

            return new Paginator<>(fetchNext, startOverWith, envelopeToListOfData, envelopeToMoreOffset,
                    loadWithParams, loadWithPaginationOffset, clearWhenStartingOver, concater,
                    distinctUntilChanged, hasTimestampOffset, limit);
        }
    }

    public @NonNull static <Data, Envelope, FirstPageParams> Builder<Data, Envelope, FirstPageParams> builder() {
        return new Builder<>();
    }

    /**
     * Returns an observable that emits the accumulated list of paginated data each time a new page is loaded.
     */
    private @NonNull Observable<List<Data>> dataWithPagination(final @NonNull Params firstPageParams) {
        final Observable<List<Data>> data = paramsAndMoreOffsetWithPagination(firstPageParams)
                .concatMap(this::fetchData);

        final Observable<List<Data>> paginatedData = clearWhenStartingOver
                ? data.scan(new ArrayList<>(), concater)
                : data.scan(concater);

        return distinctUntilChanged ? paginatedData.distinctUntilChanged() : paginatedData;
    }

    /**
     * Returns an observable that emits the params for the next page of data *or* the more URL for the next page.
     */
    private @NonNull Observable<Pair<Params, Integer>> paramsAndMoreOffsetWithPagination(final @NonNull Params firstPageParams) {
        return _moreOffset
                .map(offset -> new Pair<>(firstPageParams, offset))
                .compose(takeWhen(fetchNext))
                .startWith(new Pair<>(firstPageParams, null));
    }

    private @NonNull Observable<List<Data>> fetchData(final @NonNull Pair<Params, Integer> paginatingData) {
        try {
            return (paginatingData.second != null ?
                    loadWithPaginationOffset.apply(paginatingData.first, paginatingData.second) :
                    loadWithParams.apply(paginatingData.first))
                    .compose(neverError())
                    .doOnNext(this::keepOffset)
                    .map(envelopeToListOfData)
                    .doOnSubscribe(__ -> _isFetching.onNext(true))
                    .doAfterTerminate(() -> _isFetching.onNext(false));
        } catch (Exception ignored) {
            return Observable.empty();
        }
    }

    private void keepOffset(final @NonNull Envelope envelope) {
        if (hasTimestampOffset) {
            timestampOffset(envelope);
        } else {
            customOffset();
        }
    }

    private void customOffset() {
        _moreOffset.onNext(getOffset());
    }

    private Integer getOffset() {
        return ObjectUtils.isNotNull(_moreOffset.getValue()) ?
                _moreOffset.getValue() + limit : limit;
    }

    private void timestampOffset(final @NonNull Envelope envelope) {
        try {
            Integer offset = envelopeToMoreOffset.apply(envelope);
            if (ObjectUtils.isNotNull(offset)) {
                _moreOffset.onNext(offset);
            }
        } catch (Exception ignored) {}
    }

}
