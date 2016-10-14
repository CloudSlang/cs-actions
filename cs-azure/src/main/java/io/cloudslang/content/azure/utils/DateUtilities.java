package io.cloudslang.content.azure.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by victor on 28.09.2016.
 */
public final class DateUtilities {
    @NotNull
    public static String formatDate(@NotNull final Date date) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        return dateFormat.format(date);
    }

    @NotNull
    public static Date parseDate(@NotNull final String date) throws ParseException {
        return DateUtils.parseDate(date, "MM/dd/yyyy h:mm a");
    }
}
