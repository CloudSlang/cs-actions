package io.cloudslang.content.datetime.utils;

/**
 * Created by stcu on 22.04.2016.
 */
public class Constants {
    public static class ErrorMessages {
        public static final String DATE_NULL_OR_EMPTY = "Date is either Null or Empty";
    }

    public static class Miscellaneous {
        public static final String GMT = "GMT";
        public static final String UNIX = "unix"; //seconds since January 1, 1970
        public static final String MILLISECONDS = "S"; //milliseconds since January 1, 1970; DateTimeFormat doesn't support it

        public static final int THOUSAND_MULTIPLIER = 1000;
    }
}