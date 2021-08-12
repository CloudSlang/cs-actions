package io.cloudslang.content.rft.spike_rft;

import com.hp.oo.content.commons.util.Address;
import com.jcraft.jsch.Session;
import com.opsware.pas.content.commons.util.StringUtils;
import io.cloudslang.content.rft.spike_rft.sftp.SecureFtpAction;

import java.io.File;

/**
 * </p> Performs a SFTP Get/Put (copies a file to/from a remote host using SFTP).
 *
 * @author Cristina Nistor
 * @version 2.2, 19 May 2008, modified by Cristina Nistor
 */
public class SftpCopier extends SimpleCopier {

    public final String DEFAULT_CHARACTERSETNAME = "UTF-8";
    protected String characterSetName = DEFAULT_CHARACTERSETNAME;
    File putTmp;
    private String host;
    private int port;
    private String username;
    private String password;
    private String privateKeyFile;
    private Session cachedSession;


    public SftpCopier() {

    }

    /**
     * @return
     * @throws Exception
     */
    private SecureFtpAction connect() throws Exception {
        SecureFtpAction action = new SecureFtpAction();
        if (cachedSession != null) {
            action.connect(cachedSession);
        } else if (privateKeyFile != null && privateKeyFile.length() > 0) {
            action.connect(username, password, privateKeyFile, host, port, timeout);
        } else {
            action.connect(username, password, host, port, timeout);
        }
        return action;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    protected void getFile(String source, File getFile) throws Exception {
        SecureFtpAction action = connect();
        try {
            action.SFTPAction("get", source, getFile.getAbsolutePath(), characterSetName);
        } catch (Throwable t) {
            throw new Exception("Unable to retrieve file over SFTP.", t);
        } finally {
            action.cleanup();
        }
    }

    protected IReader getFile(String source) throws Exception {
        File getFile = File.createTempFile("SFTPCopy", ".tmp");
        getFile(source, getFile);
        return new SimpleReader(getSimpleFileName(source), getFile);
    }

    protected void putFile(IReader sourceFile, String destination) throws Exception {
        SecureFtpAction action = connect();
        try {
            action.SFTPAction("put", sourceFile.getFile().getAbsolutePath(), destination, characterSetName);
        } catch (Throwable t) {
            throw new Exception("Unable to copy file over SFTP.", t);
        } finally {
            action.cleanup();
        }

    }

    @Override
    public void setCredentials(String host, int port, String username,
                               String password) throws UnsupportedOperationException {
        Address address = new Address(host, port);
        this.host = address.getBareHost();
        this.port = address.getPort();
        this.username = username;
        this.password = password;
    }

    @Override
    public void setCredentials(String host, int port, String username,
                               String password, String privateKeyFile)
            throws UnsupportedOperationException {
        setCredentials(host, port, username, password);
        this.privateKeyFile = privateKeyFile;
    }

    public void setCustomArgument(simpleArgument name, String value) {
        switch (name) {
            case characterSet:
                if (!StringUtils.isNull(value)) characterSetName = value;
                break;
            default:
                throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
        }
    }

    public void setCustomArgument(complexArgument name, Object value) {
        switch (name) {
            case sshSession:
                if (value instanceof Session) cachedSession = (Session) value;
                break;
            default:
                throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
        }
    }

    public String getProtocolName() {
        return CopierFactory.copiers.sftp.name();
    }
}
