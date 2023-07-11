/*
 * Copyright 2021-2023 Open Text
 * This program and the accompanying materials
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

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class SmbCopier extends SimpleCopier {

    private String sourceFileName;
    private String host;
    private String domain = "";
    private String username;
    private String password;
    private DiskShare share;

    private static void inputStreamToOutputStream(InputStream in, OutputStream out)
            throws IOException {
        byte[] buf = new byte[1024000];
        try {
            int byteData = buf.length;
            while (true) {
                byteData = in.read(buf, 0, byteData);
                if (byteData == -1)
                    break;
                out.write(buf, 0, byteData);
            }

        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    @Override
    protected IReader getFile(String source) throws Exception {
        File tempFile;
        tempFile = File.createTempFile("SMBCopy", ".tmp");

        getFile(source, tempFile);
        return new SimpleReader(sourceFileName, tempFile);
    }

    @Override
    protected void getFile(String source, File destination) throws Exception {
        if (protocol.equalsIgnoreCase("smb3")) {
            String[] folders = getFormattedPathSMB2(source).split("\\\\");
            String sambaPath = folders[0];

            SmbConfig cfg = SmbConfig.builder().build();
            SMBClient client = new SMBClient(cfg);
            Connection connection = client.connect(host);
            Session session = connection.authenticate(new AuthenticationContext(username, password.toCharArray(), domain));
            share = (DiskShare) session.connectShare(sambaPath);

            StringBuilder filePath = new StringBuilder();
            for (int i = 1; i < folders.length - 1; i++) {
                filePath.append(folders[i]);
                filePath.append("\\\\");
            }
            filePath.append(folders[folders.length - 1]);

            com.hierynomus.smbj.share.File f = null;
            if (share.fileExists(filePath.toString())) {
                f = share.openFile(filePath.toString(),
                        new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL)),
                        new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
                        SMB2ShareAccess.ALL,
                        SMB2CreateDisposition.FILE_OPEN,
                        new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
            }
            if (f == null) throw new RuntimeException("The specified file doesn't exist.");
            InputStream smbInputStream = f.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            inputStreamToOutputStream(smbInputStream, fileOutputStream);
            f.close();
            connection.close();

        } else {
            String fileUrl = "smb://" + host + "/" + getFormattedPath(source);
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain,
                    username, password);
            SmbFile smbFile = new SmbFile(fileUrl, auth);
            SmbFileInputStream smbInputStream = new SmbFileInputStream(smbFile);
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            inputStreamToOutputStream(smbInputStream, fileOutputStream);
            if (sourceFileName == null)
                sourceFileName = getSimpleFileName(source);
        }
    }

    private String getFormattedPath(String path) {
        return path.replaceFirst("\\:", "\\$").replaceAll("\\\\", "/");
    }

    private String getFormattedPathSMB2(String path) {
        return path.replaceAll("/", "\\\\");
    }

    @Override
    protected void putFile(IReader sourceFile, String destination) throws Exception {
        if (protocol.equalsIgnoreCase("smb3")) {
            String[] folders = destination.replaceAll("/", "\\\\").split("\\\\");
            String sambaPath = folders[0];

            SmbConfig cfg = SmbConfig.builder().build();
            SMBClient client = new SMBClient(cfg);
            Connection connection = client.connect(host);
            Session session = connection.authenticate(new AuthenticationContext(username, password.toCharArray(), domain));

            share = (DiskShare) session.connectShare(sambaPath);
            StringBuilder filePath = new StringBuilder();
            for (int i = 1; i < folders.length - 1; i++) {
                filePath.append(folders[i]);
                if (!share.folderExists(filePath.toString()))
                    share.mkdir(filePath.toString());
                filePath.append("\\\\");
            }
            String fileName = folders[folders.length - 1];
            filePath.append(fileName);

            com.hierynomus.smbj.share.File f;
            if (!share.fileExists(filePath.toString())) {
                f = share.openFile(filePath.toString(),
                        new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL)),
                        new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
                        SMB2ShareAccess.ALL,
                        SMB2CreateDisposition.FILE_CREATE,
                        new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
            } else {
                f = share.openFile(filePath.toString(),
                        new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL)),
                        new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
                        SMB2ShareAccess.ALL,
                        SMB2CreateDisposition.FILE_OVERWRITE_IF,
                        new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
            }
            if (f == null) throw new Exception("The file creation failed.");

            InputStream fis = new FileInputStream(sourceFile.getFile());
            OutputStream smbOutputStream = f.getOutputStream();

            if (!fileName.contains(".txt")) {
                byte[] buf = new byte[1024000];
                int i;
                while ((i = fis.read(buf)) != -1) {
                    smbOutputStream.write(buf, 0, i);
                }
                fis.close();
                smbOutputStream.close();
            } else {
                inputStreamToOutputStream(fis, smbOutputStream);
            }
            f.close();
            connection.close();

        } else {
            String fileUrl = "smb://" + host + "/" + getFormattedPath(destination);
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain,
                    username, password);
            SmbFile smbFile = new SmbFile(fileUrl, auth);
            SmbFileOutputStream smbOs = new SmbFileOutputStream(smbFile);
            FileInputStream fis = new FileInputStream(sourceFile.getFile());
            inputStreamToOutputStream(fis, smbOs);
        }
    }

    @Override
    public String getProtocolName() {
        return this.protocol;
    }

    @Override
    public void setCredentials(String host, int port, String username, String password) throws UnsupportedOperationException {
        Address address = new Address(host, port);
        this.host = address.getBareHost();
        this.username = username;
        this.password = password;

    }
}
