package io.cloudslang.content.rft.spike_rft;

/**
 * �Copyright 2008-2017 EntIT Software LLC, a Micro Focus company
 */
public interface ICopier {

    /**
     * @param host
     * @param username
     * @param password
     * @throws UnsupportedOperationException
     */
    public void setCredentials(String host, int port, String username, String password) throws UnsupportedOperationException;

    ;

    /**
     * @param host
     * @param username
     * @param password
     * @param privateKeyFile
     * @throws UnsupportedOperationException
     */
    public void setCredentials(String host, int port, String username, String password, String privateKeyFile) throws UnsupportedOperationException;

    ;

    /**
     * @param destination
     * @param sourcePath
     * @param destPath
     * @throws Exception
     */
    public void copyTo(ICopier destination, String sourcePath, String destPath) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    SimpleCopier getImplementation() throws Exception;

    /**
     * @param name
     * @param value
     */
    public void setCustomArgument(simpleArgument name, String value);

    /**
     * @param name
     * @param value
     */
    public void setCustomArgument(complexArgument name, Object value);

    /**
     * @param name
     * @return
     */
    public String getCustomArgument(simpleArgument name);

    /**
     * @return
     */
    public String getProtocolName();

    /**
     * @param version
     */
    public void setVersion(String version);

    /**
     * @param timeout
     */
    public void setTimeout(int timeout);

    /**
     * @param protocol
     */
    public void setProtocol(String protocol);


    /**
     *
     *
     *
     */
    public static enum simpleArgument {
        overwrite, type, characterSet, passive
    }

    /**
     *
     *
     *
     */
    public static enum complexArgument {
        kerberosTickets, sshSession
    }
}
