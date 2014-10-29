package com.hp.score.content.ssh.utils;

/**
 * @author ioanvranauhp
 * Date: 10/29/14
 */
public class Constants {
    // inputs
    public static final String PRIVATE_KEY_FILE = "privateKeyFile";
    public static final String COMMAND = "command";
    public static final String ARGS = "arguments";
    public static final String PTY = "pty";
    public static final String CHARACTER_DELAY = "characterDelay";
    public static final String NEWLINE_SEQUENCE = "newlineCharacters";
    public static final String FORWARD_LOCAL_PORT = "forwardLocalPort";
    public static final String FORWARD_REMOTE_HOST = "forwardRemoteHost";
    public static final String FORWARD_REMOTE_PORT = "forwardRemotePort";
    public static final String SESSION_ID = "sessionId";
    public static final String SSH_SESSIONS_DEFAULT_ID = "sshSessions:default-id";
    public static final String USE_GLOBAL_CONTEXT = "useGlobalContext";
    public static final String CLOSE_SESSION = "closeSession";
    // outputs
    public static final String STDOUT = "STDOUT";
    public static final String STDERR = "STDERR";
    public static final String VISUALIZED = "visualized";
    // default values
    public static final int DEFAULT_PORT = 22;
    public static final int DEFAULT_TIMEOUT = 90000; //90 seconds
    public static final int DEFAULT_READ_TIMEOUT = 20000; //20 seconds
    public static final int DEFAULT_MATCH_TIMEOUT = 1000; //1 second
    public static final int DEFAULT_IDLE_TIMEOUT = 5;
    public static final int DEFAULT_WRITE_CHARACTER_TIMEOUT = 0;
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000; //10 seconds
    public static final boolean DEFAULT_USE_PSEUDO_TERMINAL = false;
    public static final String DEFAULT_NEWLINE = "\\n";
    public static final String DEFAULT_CHARACTER_SET = "UTF-8";
    public static final boolean DEFAULT_CLOSE_SESSION = false;
    public static final boolean DEFAULT_USE_GLOBAL_CONTEXT = false;
    // errors
    public static final String ARGS_IS_DEPRECATED = "This input is deprecated, use the command input to provide arguments.";

    //we add this temporary (until platform provide us the constants)
    public static final class OutputNames {

        public static final String RETURN_RESULT = "";
        public static final String EXCEPTION = "";
        public static final String RETURN_CODE = "";
    }

    public static final class ReturnCodes {

        public static final String RETURN_CODE_FAILURE = "";
        public static final String RETURN_CODE_SUCCESS = "";
    }

    public static final class ResponseNames {

        public static final String SUCCESS = "";
        public static final String FAILURE = "";
    }

    public static final class InputNames {

        public static final String HOST = "";
        public static final String PORT = "";
        public static final String USERNAME = "";
        public static final String PASSWORD = "";
        public static final String CHARACTER_SET = "";
        public static final String TIMEOUT = "";
    }


}
