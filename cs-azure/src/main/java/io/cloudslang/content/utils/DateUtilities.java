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
    public static String getDateWithOffset(final int offset) {
        final DateFormat dateFormat = new SimpleDateFormat("MM/DD/YYYY H:MM PM|AM");
        final Date offsetDate = DateUtils.addSeconds(new Date(), offset);
        return dateFormat.format(offsetDate);
    }
}
