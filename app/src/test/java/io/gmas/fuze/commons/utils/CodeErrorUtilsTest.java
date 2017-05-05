package io.gmas.fuze.commons.utils;

import org.junit.Test;

import io.gmas.fuze.FZRobolectricTestCase;

public final class CodeErrorUtilsTest extends FZRobolectricTestCase {

    @Test
    public void testIsUnauthorized() {
        assertTrue(CodeErrorUtils.isUnauthorized(401));
        assertFalse(CodeErrorUtils.isUnauthorized(200));
    }

    @Test
    public void testIsBadRequest() {
        assertTrue(CodeErrorUtils.isBadRequest(400));
        assertFalse(CodeErrorUtils.isBadRequest(200));
    }

    @Test
    public void testIsForbidden() {
        assertTrue(CodeErrorUtils.isForbidden(403));
        assertFalse(CodeErrorUtils.isForbidden(200));
    }

    @Test
    public void testIsNotFound() {
        assertTrue(CodeErrorUtils.isNotFound(404));
        assertFalse(CodeErrorUtils.isNotFound(200));
    }

    @Test
    public void testIsBadGateway() {
        assertTrue(CodeErrorUtils.isBadGateway(503));
        assertFalse(CodeErrorUtils.isBadGateway(200));
    }

    @Test
    public void testIsInternalServerError() {
        assertTrue(CodeErrorUtils.isInternalServerError(500));
        assertFalse(CodeErrorUtils.isInternalServerError(200));
    }

}
