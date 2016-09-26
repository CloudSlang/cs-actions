package io.cloudslang.content.ssh.exceptions;

/**
 * @author hasna
 * @since 06.04.2015
 */
public class SSHException extends Exception {
    public SSHException(String message) {
        super(message);
    }

    public SSHException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHException(Throwable cause) {
        super(cause);
    }
}
