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
package io.cloudslang.content.rft.services;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.rft.entities.FTPException;
import io.cloudslang.content.rft.entities.FTPInputs;
import io.cloudslang.content.rft.utils.FTPOperation;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.rft.utils.Inputs.FTPInputs.PARAM_TYPE;
import static io.cloudslang.content.rft.utils.Constants.*;

public class FTPService {

    /**
     * last ftp reply code is set here. This makes the class non-reentrant!
     */
    private int _replyCode = 200;
    /**
     * session log is accumulated in this buffer. This makes the class non-reentrant!
     */
    private StringBuffer _sessionLog = new StringBuffer();

    private static boolean isNull(String s) {
        return s == null || s.isEmpty();
    }

    private static void close(FTPClient ftp) {
        if (ftp != null) {
            try {
                ftp.disconnect();
            } catch (Exception ignore) {
            }
        }
    }

    private static void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception ignore) {
            }
        }
    }

    private static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception ignore) {
            }
        }
    }

    private int getReplyCode() {
        return _replyCode;
    }

    private String getSessionLog() {
        return _sessionLog.toString();
    }

    public Map<String, String> ftpOperation(FTPInputs ftpInputs, FTPOperation ftpOperation) {
        FTPClient ftp = null;
        final Map<String, String> result = new HashMap<>();

        try {
            ftp = connect(ftpInputs.getHostname(), Integer.parseInt(ftpInputs.getPort()), ftpInputs.getCharacterSet(), Boolean.parseBoolean(ftpInputs.getPassive()));

            login(ftp, ftpInputs.getUser(), ftpInputs.getPassword());
            setFileType(ftp, ftpInputs.getType());
            performFTPOperation(ftp, ftpOperation, ftpInputs.getRemoteFile(), ftpInputs.getLocalFile());

            result.put(OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
            result.put(OutputNames.RETURN_RESULT, SUCCESS_RESULT);

        } catch (Exception e) {
            result.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
            result.put(OutputNames.RETURN_RESULT, e.getMessage());
            result.put(OutputNames.EXCEPTION, e.getMessage());
        } finally {
            result.put(FTP_REPLY_CODE, String.valueOf(getReplyCode()));
            result.put(FTP_SESSION_LOG, getSessionLog());
            close(ftp);
            return result;
        }
    }

    private void performFTPOperation(FTPClient ftp, FTPOperation ftpOperation, String remoteFilePath, String localFilePath) throws IOException, FTPException {
        if (ftpOperation == FTPOperation.GET) {
            retrieveFile(ftp, remoteFilePath, localFilePath);
        } else if (ftpOperation == FTPOperation.PUT) {
            putFile(ftp, remoteFilePath, localFilePath);
        } else throw new FTPException("Invaid FTP Operation");
    }

    private void putFile(FTPClient ftp, String remoteFilePath, String localFilePath) throws IOException, FTPException {

        InputStream in = null;
        try {
            in = getLocalFileInputStream(localFilePath);
            putFile(ftp, remoteFilePath, in);
        } catch (FileNotFoundException ex) {
            throw new FTPException(String.format(EXCEPTION_INVALID_LOCAL_FILE, localFilePath), ex);
        } finally {
            close(in);
        }
    }

    private InputStream getLocalFileInputStream(String localFilePath) throws FTPException {
        try {
            return new BufferedInputStream(new FileInputStream(new File(localFilePath)));
        } catch (FileNotFoundException ex) {
            throw new FTPException(String.format(EXCEPTION_INVALID_LOCAL_FILE, localFilePath), ex);
        }
    }

    private void putFile(FTPClient ftp, String remoteFilePath, InputStream in)
            throws IOException, FTPException {
        ftp.storeFile(remoteFilePath, in);
        checkReply("put " + remoteFilePath, ftp);
    }

    private void retrieveFile(FTPClient ftp, String remoteFilePath, String localFilePath) throws IOException, FTPException {

        OutputStream out = null;
        try {
            if (!remoteFileExists(ftp, remoteFilePath)) {
                throw new FTPException(String.format(EXCEPTION_INVALID_REMOTE_FILE, remoteFilePath));
            } else {
                out = getLocalFileOutputStream(localFilePath);
                retrieveFile(ftp, remoteFilePath, out);
            }

        } catch (FileNotFoundException ex) {
            throw new FTPException(String.format(EXCEPTION_INVALID_LOCAL_FILE, localFilePath), ex);
        } finally {
            close(out);
        }
    }

    private void setFileType(FTPClient ftp, String type) throws IOException, FTPException {

        if (isNull(type) || type.equalsIgnoreCase("binary")) {
            setImageFileType(ftp);
        } else if (type.equalsIgnoreCase("ascii")) {
            setASCIIFileType(ftp);
        } else {
            throw new FTPException(String.format(EXCEPTION_INVALID_TYPE, type, PARAM_TYPE));
        }
    }

    private void setImageFileType(FTPClient ftp) throws IOException, FTPException {

        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        checkReply("bin", ftp);
    }

    private void setASCIIFileType(FTPClient ftp) throws IOException, FTPException {
        ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
        checkReply("asc", ftp);
    }

    private void retrieveFile(FTPClient ftp, String remoteFilePath, OutputStream out) throws IOException, FTPException {
        ftp.retrieveFile(remoteFilePath, out);
        checkReply("get " + remoteFilePath, ftp);
    }

    private boolean remoteFileExists(FTPClient ftpClient, String filePath) throws IOException {
        return ftpClient.listFiles(filePath).length != 0;
    }

    private OutputStream getLocalFileOutputStream(String localFilePath) throws FTPException {
        try {
            return new BufferedOutputStream(new FileOutputStream(new File(localFilePath)));
        } catch (FileNotFoundException ex) {
            throw new FTPException(String.format(EXCEPTION_INVALID_LOCAL_FILE, localFilePath), ex);
        }
    }

    protected FTPClient connect(String server, int portNumber) throws IOException,
            FTPException {
        return connect(server, portNumber, null, false);
    }

    private FTPClient connect(String server, int portNumber, String encoding, boolean passive) throws IOException,
            FTPException {
        FTPClient ftp = createFtpClient();
        if (!isNull(encoding)) {
            ftp.setControlEncoding(encoding);
        } //else use default which is ISO-8859-1 (Latin-1)

        try {
            ftp.connect(InetAddress.getByName(server), portNumber);
            if (passive) {
                //enable passive connections.  The default is active.  Must be done after connection is established.
                ftp.enterLocalPassiveMode();
            }
        } catch (ConnectException ex) {
            throw new FTPException(String.format(EXCEPTION_CONNECT, server, portNumber, ex.getMessage()), ex);
        } catch (UnknownHostException ex) {
            throw new FTPException(String.format(EXCEPTION_UNKNOWN_HOST, server), ex);
        }
        checkReply("open " + server + " " + portNumber, ftp);
        return ftp;
    }

    private void login(FTPClient ftp, String user, String password) throws FTPException, IOException {
        ftp.login(user, password);
        checkReply("user " + user, ftp);
    }

    private void checkReplyCode(String command, int reply, FTPClient ftp) throws FTPException {
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new FTPException(reply, ftp.getReplyString());
        }
    }

    private void checkReply(String command, FTPClient ftp) throws FTPException {
        _sessionLog.append("> ").append(command).append("\r\n");
        _sessionLog.append(ftp.getReplyString()).append("\r\n");
        _replyCode = ftp.getReplyCode();
        checkReplyCode(command, _replyCode, ftp);
    }

    private FTPClient createFtpClient() {
        return new FTPClient();
    }

}
