package org.openscore.content.socialnetworks.utils;

/**
 * Created by vranau on 1/12/2015.
 */
public class Constants {

    public static final String STDOUT = "STDOUT";
    public static final String STDERR = "STDERR";
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

    public static final class InputNames {

        public static final String HASHTAG = "hashTag";
        public static final String START_DATE = "startDate";
        public static final String CONSUMER_KEY_STR = "consumerKeyStr";
        public static final String CONSUMER_SECRET_STR = "consumerSecretStr";
        public static final String ACCESS_TOKEN_STR = "accessTokenStr";
        public static final String ACCESS_TOKEN_SECRET_STR = "accessTokenSecretStr";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
    }
}
