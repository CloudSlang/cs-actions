

package io.cloudslang.content.vmware.connection.exceptions;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class ConnectionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
