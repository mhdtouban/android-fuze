package io.gmas.fuze.commons.utils;

import org.joda.time.DateTime;
import org.junit.Test;

import io.gmas.fuze.FZRobolectricTestCase;

public final class DateUtilsTest extends FZRobolectricTestCase {

    @Test
    public void testStringToDate() {
        String date = "2017-02-27T06:30:00+01:00";
        DateTime dateTime = DateUtils.stringToDateTime(date);
        assertNotNull(dateTime);
        assertEquals(DateUtils.dateToString(dateTime), "27/02/2017");
    }

    @Test
    public void testDateToString() {
        String date = "2017-02-27T06:30:00+01:00";
        DateTime dateTime = DateUtils.stringToDateTime(date);
        assertNotNull(dateTime);
        assertEquals(DateUtils.dateToString(dateTime), "27/02/2017");
    }

}
