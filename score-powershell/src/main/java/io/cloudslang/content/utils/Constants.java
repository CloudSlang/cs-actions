package io.cloudslang.content.utils;

/**
 * User: bancl
 * Date: 10/6/2015
 */
public class Constants {

    public static final class InputNames {
        public static final String INPUT_HOST = "host";
        public static final String INPUT_USERNAME = "username";
        public static final String INPUT_PASSWORD = "password";
        public static final String INPUT_SCRIPT = "script";
        public static final String ENABLE_HTTPS = "enableHTTPS";
    }

    public static final class OutputNames {
        public static final String RETURN_RESULT = "returnResult";
        public static final String EXCEPTION = "exception";
        public static final String RETURN_CODE = "returnCode";
    }

    public static final class ReturnCodes {
        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }

    public static final class ResponseNames {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

}