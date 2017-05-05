package io.gmas.fuze.commons.utils;

import org.junit.Test;

import io.gmas.fuze.FZRobolectricTestCase;

public final class StringUtilsTest extends FZRobolectricTestCase {

    @Test
    public void testIsEmail() {
        assertTrue(StringUtils.isEmail("android@yummypets.com"));
        assertFalse(StringUtils.isEmail("android@yummypets"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(" "));
        assertTrue(StringUtils.isEmpty("     "));
        assertTrue(StringUtils.isEmpty(null));
        assertFalse(StringUtils.isEmpty("a"));
        assertFalse(StringUtils.isEmpty(" a "));
    }

    @Test
    public void testIsPresent() {
        assertFalse(StringUtils.isPresent(""));
        assertFalse(StringUtils.isPresent(" "));
        assertFalse(StringUtils.isPresent("     "));
        assertFalse(StringUtils.isPresent(null));
        assertTrue(StringUtils.isPresent("a"));
        assertTrue(StringUtils.isPresent(" a "));
    }

}