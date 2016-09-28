package io.cloudslang.content.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by victor on 28.09.2016.
 */
public class DateUtilities {
    @NotNull
    public static String formatDate(@NotNull final Date date) {
        final DateFormat dateFormat = new SimpleDateFormat("MM/DD/YYYY H:MM PM|AM");
        return dateFormat.format(date);
    }

    @NotNull
    public static Date getDateWithOffset(final int offset) {
        return DateUtils.addSeconds(new Date(), offset);
    }
}
