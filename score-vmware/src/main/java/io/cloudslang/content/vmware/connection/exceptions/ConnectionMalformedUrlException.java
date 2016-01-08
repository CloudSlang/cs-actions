package io.cloudslang.content.vmware.connection.exceptions;

import java.net.MalformedURLException;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class ConnectionMalformedUrlException extends ConnectionException {
    private static final long serialVersionUID = 1L;
    public ConnectionMalformedUrlException(String url, MalformedURLException e) {
        super(url, e);
    }
}