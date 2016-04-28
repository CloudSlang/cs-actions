package io.cloudslang.content.datetime.utils;

/**
 * Created by stcu on 22.04.2016.
 */
public class Constants {

    public static class OutputNames {
        public static final String RETURN_RESULT = "returnResult";
        public static final String EXCEPTION = "exception";
        public static final String RETURN_CODE = "returnCode";
        public static final String ERROR_MESSAGE = "errorMessage";
    }

    public static class ReturnCodes {
        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }

    public static class ResponseNames {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static class InputNames {
        public static final String LOCALE_LANG = "localeLang";
        public static final String LOCALE_COUNTRY = "localeCountry";
        public static final String LOCALE_DATE = "date";
        public static final String LOCALE_OFFSET = "offset";
        public static final String DATE_FORMAT = "dateFormat";
        public static final String DATE_LOCALE_LANG = "dateLocaleLang";
        public static final String DATE_LOCALE_COUNTRY = "dateLocaleCountry";
        public static final String OUT_FORMAT = "outputFormat";
        public static final String OUT_LOCALE_LANG = "outLocaleLang";
        public static final String OUT_LOCALE_COUNTRY = "outLocaleCountry";
    }
}
