package io.cloudslang.content.datetime.utils;

/**
 * Created by ursan on 7/6/2016.
 */
public class DateTimeUtils {
    public static boolean isUnix(String locale) {
        return Constants.Miscellaneous.UNIX.equals(locale);
    }
}
