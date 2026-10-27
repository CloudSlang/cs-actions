/*
 * Copyright 2026-2027 Open Text
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.EnumSet;

public class SmbCopier extends SimpleCopier {

    private String host;
    private String domain = "";
    private String username;
    private String password;

    private static void inputStreamToOutputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024 * 1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    @Override
    protected IReader getFile(String source) throws Exception {
        File tempFile = File.createTempFile("SMBCopy", ".tmp");
        getFile(source, tempFile);
        return new SimpleReader(getSimpleFileName(source), tempFile);
    }

    @Override
    protected void getFile(String source, File destination) throws Exception {
        SmbPath smbPath = parsePath(source);
        try (SMBClient client = new SMBClient(SmbConfig.builder().build());
             Connection connection = client.connect(host);
             Session session = connection.authenticate(
                     new AuthenticationContext(username, password.toCharArray(), domain));
             DiskShare share = (DiskShare) session.connectShare(smbPath.shareName())) {
            if (!share.fileExists(smbPath.relativePath())) {
                throw new FileNotFoundException("The specified file doesn't exist: " + source);
            }
            try (com.hierynomus.smbj.share.File smbFile = share.openFile(
                    smbPath.relativePath(),
                    EnumSet.of(AccessMask.GENERIC_READ),
                    EnumSet.of(FileAttributes.FILE_ATTRIBUTE_NORMAL),
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN,
                    EnumSet.of(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));
                 InputStream smbInputStream = smbFile.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
                inputStreamToOutputStream(smbInputStream, fileOutputStream);
            }
        }
    }

    private SmbPath parsePath(String path) {
        String[] segments = Arrays.stream(path.replace('/', '\\').split("\\\\"))
                .filter(segment -> !segment.isEmpty())
                .toArray(String[]::new);
        if (segments.length < 2) {
            throw new IllegalArgumentException("The SMB path must include a share and a file path.");
        }
        return new SmbPath(segments[0], String.join("\\", Arrays.copyOfRange(segments, 1, segments.length)));
    }

    @Override
    protected void putFile(IReader sourceFile, String destination) throws Exception {
        SmbPath smbPath = parsePath(destination);
        String[] pathSegments = smbPath.relativePath().split("\\\\");
        String fileName = pathSegments[pathSegments.length - 1];
        StringBuilder directoryPath = new StringBuilder();
        try (SMBClient client = new SMBClient(SmbConfig.builder().build());
             Connection connection = client.connect(host);
             Session session = connection.authenticate(
                     new AuthenticationContext(username, password.toCharArray(), domain));
             DiskShare share = (DiskShare) session.connectShare(smbPath.shareName())) {
            for (int i = 0; i < pathSegments.length - 1; i++) {
                if (directoryPath.length() > 0) {
                    directoryPath.append('\\');
                }
                directoryPath.append(pathSegments[i]);
                if (!share.folderExists(directoryPath.toString())) {
                    share.mkdir(directoryPath.toString());
                }
            }

            try (com.hierynomus.smbj.share.File smbFile = share.openFile(
                    smbPath.relativePath(),
                    EnumSet.of(AccessMask.GENERIC_WRITE),
                    EnumSet.of(FileAttributes.FILE_ATTRIBUTE_NORMAL),
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OVERWRITE_IF,
                    EnumSet.of(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));
                 FileInputStream fileInputStream = new FileInputStream(sourceFile.getFile());
                 OutputStream smbOutputStream = smbFile.getOutputStream()) {
                inputStreamToOutputStream(fileInputStream, smbOutputStream);
            }
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

    private record SmbPath(String shareName, String relativePath) {
    }
}
