/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.rft.remote_copy;

import com.hp.oo.content.commons.util.Address;
import com.jcraft.jsch.Session;
import com.opsware.pas.content.commons.util.StringUtils;
import io.cloudslang.content.rft.remote_copy.sftp.SecureFtpAction;

import java.io.File;

/**
 * Performs a SFTP Get/Put (copies a file to/from a remote host using SFTP).
 *
 */
public class SftpCopier extends SimpleCopier {

    public final String DEFAULT_CHARACTERSETNAME = "UTF-8";
    protected String characterSetName = DEFAULT_CHARACTERSETNAME;
    File putTmp;
    private String host;
    private int port;
    private String username;
    private String password;
    private int connectionTimeout;
    private int executionTimeout;
    private String proxyHost;
    private int proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String privateKeyFile;
    private Session cachedSession;


    public SftpCopier() {
    }

    private SecureFtpAction connect() throws Exception {
        SecureFtpAction action = new SecureFtpAction();
        if (cachedSession != null) {
            action.connect(cachedSession);
        } else if (privateKeyFile != null && privateKeyFile.length() > 0) {
            action.connect(username, password, privateKeyFile, host, port, connectionTimeout, executionTimeout, proxyHost,
                    proxyPort, proxyUsername, proxyPassword);
        } else {
            action.connect(username, password, host, port, connectionTimeout, executionTimeout, proxyHost, proxyPort,
                    proxyUsername, proxyPassword);
        }
        return action;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setExecutionTimeout(int executionTimeout) {
        this.executionTimeout = executionTimeout;
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

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }
}
