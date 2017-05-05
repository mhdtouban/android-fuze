package io.gmas.fuze.commons.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.gmas.fuze.FZRobolectricTestCase;

public final class ListUtilsTest extends FZRobolectricTestCase {

    @Test
    public void testFirst() {
        List<String> list = new ArrayList<>();
        assertNull(ListUtils.first(list));
        list.add("android");
        assertEquals(ListUtils.first(list), "android");
    }

}
