package io.cloudslang.content.utils;

/**
 * Created by giloan on 3/27/2016.
 */
public class Constants {

    public static final class InputNames {
        public static final String INPUT_HOST = "host";
        public static final String INPUT_PORT = "port";
        public static final String INPUT_SCRIPT = "script";
        public static final String MAX_ENVELOP_SIZE = "winrmMaxEnvelopSize";
        public static final String WINRM_LOCALE = "winrmLocale";
        public static final String PROTOCOL = "protocol";
        public static final String OPERATION_TIMEOUT = "operationTimeout";
    }

    public static final class OutputNames {
        public static final String RETURN_RESULT = "returnResult";
        public static final String SCRIPT_EXIT_CODE = "scriptExitCode";
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