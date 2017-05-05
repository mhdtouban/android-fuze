package io.gmas.fuze.commons.utils;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

    /**
     * Convert DateTime to String with DateTimeFormat pattern -> dd/MM/yyyy
     *
     * @param date DateTime
     * @return String
     */
    public static @NonNull String dateToString(final @NonNull DateTime date) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        return fmt.print(date);
    }

    public static @NonNull String dateToStringApiFormat(final @NonNull DateTime date) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        return fmt.print(date);
    }

    /**
     * Convert String to DateTime
     *
     * @param date String
     * @return DateTime
     */
    public static @NonNull DateTime stringToDateTime(final @NonNull String date) {
        DateTime dt = DateTime.parse(date);
        dt.toDateTime(DateTimeZone.UTC);
        return dt;
    }

}
