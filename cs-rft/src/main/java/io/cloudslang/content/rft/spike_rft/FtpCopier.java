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
package io.cloudslang.content.rft.spike_rft;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * </p> Performs a FTP Get/Post (copies a file to/from a remote host using FTP).
 * �Copyright 2008-2017 EntIT Software LLC, a Micro Focus company
 *
 * @author Cristina Nistor
 * @version 2.1, 19 May 2008, modified by Cristina Nistor
 */
public class FtpCopier extends SimpleCopier {

    /**
     * last ftp reply code is set here. This makes the class non-reentrant!
     */
    protected int _replyCode = 200;
    protected int DEFAULT_FTP_PORT = 21;
    protected String characterSetName = null; //Apache Commons FTP will use the default (Latin-1) when this is null
    /**
     * session log is accumulated in this buffer. This makes the class non-reentrant!
     */
    protected StringBuffer _sessionLog = new StringBuffer();
    private String host;
    private int port;
    private String username;
    private String password;
    private String type;
    private boolean passive = false;

    public FtpCopier() {

    }

    protected static final void close(FTPClient ftp) {
        if (ftp != null) {
            try {
                ftp.disconnect();
            } catch (Exception ignore) {
            }
        }
    }

    protected static final void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception ignore) {
            }
        }
    }

    protected static final void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception ignore) {
            }
        }
    }

    protected IReader getFile(String source) throws Exception {
        FTPClient client = null;
        File getFile;
        String[] filePath = source.split("\\.");
        String fileExt = "";
        if (filePath.length > 1) {
            fileExt = "." + filePath[filePath.length - 1];
        }
        getFile = File.createTempFile("FTPCopy", fileExt);
        try {
            client = connect(host, port);
            login(client, username, password);
            performGetFTPAction(client, source, getFile);
        } catch (Throwable t) {
            throw new Exception("Failed to Get the File over FTP", t);
        } finally {
            close(client);
        }
        return new SimpleReader(getSimpleFileName(source), getFile);
    }

    protected void getFile(String source, File getFile) throws Exception {
        FTPClient client = null;
        try {
            client = connect(host, port);
            login(client, username, password);
            performGetFTPAction(client, source, getFile);
        } catch (Throwable t) {
            throw new Exception("Failed to Get the File over FTP", t);
        } finally {
            close(client);
        }
    }

    protected void putFile(IReader sourceFile, String destination) throws Exception {
        FTPClient client = null;
        try {
            client = connect(host, port);
            login(client, username, password);
            performPutFTPAction(client, sourceFile.getFile(), destination);
        } finally {
            close(client);
        }
    }

    @Override
    public void setCredentials(String host, int port, String username, String password) throws UnsupportedOperationException {
        Address address = new Address(host, port);
        this.host = address.getBareHost();
        this.port = (address.getPort() != Address.PORT_NOT_SET) ? address.getPort() : DEFAULT_FTP_PORT;
        this.username = username;
        this.password = password;
    }

    @Override
    public void setCustomArgument(simpleArgument name, String value) {
        switch (name) {
            case type:
                type = value;
                break;
            case characterSet:
                characterSetName = value;
                break;
            case passive:
                passive = Boolean.valueOf(value);
                break;
            default:
                throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
        }
    }

    protected void performGetFTPAction(FTPClient ftp, String srcPath, File tmp) throws IOException, Exception {
        if (type.equalsIgnoreCase("ascii")) {
            setASCIIFileType(ftp);
        } else {
            setImageFileType(ftp);
        }
        retrieveFile(ftp, srcPath, tmp);
    }

    protected void retrieveFile(FTPClient ftp, String remoteFilePath, File localFilePath)
            throws IOException, Exception {

        OutputStream out = null;
        try {
            out = getLocalFileOutputStream(localFilePath);
            retrieveFile(ftp, remoteFilePath, out);
        } catch (FileNotFoundException ex) {
            throw new Exception("invalid local file path: " + localFilePath, ex);
        } finally {
            close(out);
        }
    }

    protected OutputStream getLocalFileOutputStream(File localFilePath) throws Exception {
        try {
            return new BufferedOutputStream(new FileOutputStream(localFilePath));
        } catch (FileNotFoundException ex) {
            throw new Exception("invalid local file path: " + localFilePath, ex);
        }
    }

    protected void retrieveFile(FTPClient ftp, String remoteFilePath, OutputStream out)
            throws IOException, Exception {
        ftp.retrieveFile(remoteFilePath, out);
        checkReply("get " + remoteFilePath, ftp);
    }

    protected void performPutFTPAction(FTPClient ftp, File tmp, String destPath) throws IOException, Exception {
        if (type.equalsIgnoreCase("ascii")) {
            setASCIIFileType(ftp);
        } else {
            setImageFileType(ftp);
        }
        putFile(ftp, destPath, tmp);
    }

    protected void putFile(FTPClient ftp, String remoteFilePath, File localFile) throws IOException, Exception {
        InputStream in = null;
        try {
            in = getLocalFileInputStream(localFile);
            putFile(ftp, remoteFilePath, in);
        } catch (FileNotFoundException ex) {
            throw new Exception("invalid local file path: " + localFile.getAbsolutePath(), ex);
        } finally {
            close(in);
        }
    }

    protected InputStream getLocalFileInputStream(File localFile) throws Exception {
        try {
            return new BufferedInputStream(new FileInputStream(localFile));
        } catch (FileNotFoundException ex) {
            throw new Exception("invalid local file path: " + localFile.getAbsolutePath(), ex);
        }
    }

    protected void putFile(FTPClient ftp, String remoteFilePath, InputStream in) throws IOException, Exception {
        ftp.storeFile(remoteFilePath, in);
        checkReply("put " + remoteFilePath, ftp);
    }

    protected int getReplyCode() {
        return _replyCode;
    }

    protected String getSessionLog() {
        return _sessionLog.toString();
    }

    protected void setImageFileType(FTPClient ftp) throws IOException, Exception {
        //int replyCode = ftp.type(FTPClient.IMAGE_FILE_TYPE);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        checkReply("bin", ftp);
    }

    protected void setASCIIFileType(FTPClient ftp) throws IOException, Exception {
        //int replyCode = ftp.type(FTPClient.ASCII_FILE_TYPE);
        ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
        checkReply("asc", ftp);
    }

    protected FTPClient connect(String server, int portNumber) throws SocketException, IOException,
            Exception {
        FTPClient ftp = new FTPClient();
        if (!StringUtils.isEmpty(characterSetName))
            ftp.setControlEncoding(characterSetName);
        try {
            ftp.connect(InetAddress.getByName(server), portNumber);
            if (passive) {
                //enable passive connections.  The default is active.  Must be done after connection is established.
                ftp.enterLocalPassiveMode();
            }
        } catch (ConnectException ex) {
            throw new Exception("could not connect to "
                    + server
                    + ":"
                    + portNumber
                    + ", reason: "
                    + ex.getMessage(), ex);
        } catch (UnknownHostException ex) {
            throw new Exception("unknown host " + server, ex);
        }
        checkReply("open " + server + " " + portNumber, ftp);
        return ftp;
    }

    protected void login(FTPClient ftp, String user, String password) throws Exception,
            SocketException, IOException {
        ftp.login(user, password);
        checkReply("user " + user, ftp);
    }

    protected int checkReplyCode(String command, int reply, FTPClient ftp) throws Exception {
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new Exception(ftp.getReplyString());
        }
        return reply;
    }

    protected int checkReply(String command, FTPClient ftp) throws Exception {
        _sessionLog.append("> ").append(command).append("\r\n");
        _sessionLog.append(ftp.getReplyString()).append("\r\n");
        _replyCode = ftp.getReplyCode();
        return checkReplyCode(command, _replyCode, ftp);
    }

    public String getProtocolName() {
        return CopierFactory.copiers.ftp.name();
    }
}
