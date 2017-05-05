package io.gmas.fuze.commons.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {
    private ListUtils() {
        throw new AssertionError();
    }

    /**
     * Returns the first object or `null` if empty.
     */
    public static @Nullable <T> T first(final @NonNull List<T> xs) {
        return xs.size() > 0 ? xs.get(0) : null;
    }

    /**
     * Concats the second argument onto the end of the first without mutating
     * either list.
     */
    public static <T> List<T> concat(final @NonNull List<T> xs, final @NonNull List<T> ys) {
        final List<T> zs = new ArrayList<>(xs);
        ListUtils.mutatingConcat(zs, ys);
        return zs;
    }

    /**
     * Concats the distinct elements of `ys` onto the end of the `xs` without mutating either list.
     */
    public static <T> List<T> concatDistinct(final @NonNull List<T> xs, final @NonNull List<T> ys) {
        final List<T> zs = new ArrayList<>(xs);
        ListUtils.mutatingConcatDistinct(zs, ys);
        return zs;
    }

    /**
     * Concats the second argument onto the end of the first, but also mutates the
     * first argument.
     */
    public static <T> List<T> mutatingConcat(final @NonNull List<T> xs, final @NonNull List<T> ys) {
        xs.addAll(ys);
        return xs;
    }

    /**
     * Concats the distinct elements of `ys` onto the end of the `xs`, but also mutates the first.
     */
    public static <T> List<T> mutatingConcatDistinct(final @NonNull List<T> xs, final @NonNull List<T> ys) {
        for (final T y : ys) {
            if (!xs.contains(y)) {
                xs.add(y);
            }
        }
        return xs;
    }

    public static <T> boolean isEmpty(final @NonNull List<T> list) {
        return list.size() == 0;
    }

    public static <T> boolean isNotEmpty(final @NonNull List<T> list) {
        return !isEmpty(list);
    }

    public static <T> List<T> empty() {
        return new ArrayList<>();
    }

    public static <T> List<T> reverse(final @NonNull List<T> list) {
        Collections.reverse(list);
        return list;
    }

    public static @NonNull <T> List<T> toList(T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        return list;
    }

}
