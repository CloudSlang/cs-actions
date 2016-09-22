package io.cloudslang.content.ssh.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class Constants {
    public static final String EMPTY_STRING = "";
    // inputs
    public static final String PRIVATE_KEY_DATA = "privateKeyData";
    public static final String PRIVATE_KEY_FILE = "privateKeyFile";
    public static final String COMMAND = "command";
    public static final String ARGS = "arguments";
    public static final String PTY = "pty";
    public static final String SSH_SESSIONS_DEFAULT_ID = "sshSessions:default-id";
    public static final String CLOSE_SESSION = "closeSession";
    public static final String KNOWN_HOSTS_POLICY = "knownHostsPolicy";
    public static final String KNOWN_HOSTS_PATH = "knownHostsPath";
    public static final String ALLOWED_CIPHERS = "allowedCiphers";
    public static final String ALLOW_EXPECT_COMMANDS = "allowExpectCommands";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";
    public static final String CONNECT_TIMEOUT = "connectTimeout";
    // outputs
    public static final String STDOUT = "STDOUT";
    public static final String STDERR = "STDERR";
    public static final String EXIT_STATUS = "exitStatus";
    // default values
    public static final int DEFAULT_PORT = 22;
    public static final int DEFAULT_PROXY_PORT = 8080;
    public static final boolean DEFAULT_ALLOW_EXPECT_COMMANDS = false;
    public static final int DEFAULT_TIMEOUT = 90000; //90 seconds
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000; //10 seconds
    public static final boolean DEFAULT_USE_PSEUDO_TERMINAL = false;
    public static final boolean DEFAULT_USE_AGENT_FORWARDING = false;
    public static final String DEFAULT_NEWLINE = "\\n";
    public static final String DEFAULT_CHARACTER_SET = "UTF-8";
    public static final boolean DEFAULT_CLOSE_SESSION = false;
    public static final String DEFAULT_KNOWN_HOSTS_POLICY = "allow";
    public static final Path DEFAULT_KNOWN_HOSTS_PATH = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");

    // errors
    public static final String ARGS_IS_DEPRECATED = "This input is deprecated, use the command input to provide arguments.";

    public static final class InputNames {

        public static final String HOST = "host";
        public static final String PORT = "port";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String CHARACTER_SET = "characterSet";
        public static final String TIMEOUT = "timeout";
        public static final String AGENT_FORWARDING = "agentForwarding";
    }
}
