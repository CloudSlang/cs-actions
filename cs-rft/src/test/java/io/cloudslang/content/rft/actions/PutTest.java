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
package io.cloudslang.content.rft.actions;


import io.cloudslang.content.rft.actions.ftp.Put;
import io.cloudslang.content.rft.services.FTPService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.File;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;

import static io.cloudslang.content.rft.utils.ftp.Constants.EXCEPTION_INVALID_LOCAL_FILE;
import static io.cloudslang.content.rft.utils.ftp.Constants.SUCCESS_RESULT;
import static org.junit.Assert.*;

public class PutTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    Put putOperation;
    FakeFtpServer fakeFtpServer;
    FTPService ftpService;
    File localFile;
    FileSystem fileSystem;

    @Before
    public void setUp() throws Exception {

        fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));

        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(0);
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));
        fakeFtpServer.start();

        putOperation = new Put();
        ftpService = new FTPService();
        localFile = temporaryFolder.newFile("localfile.txt");

    }


    @Test
    public void executeWithSuccess() throws Exception {
        String localFilePath = localFile.getCanonicalPath();

        Map<String, String> result = putOperation.execute("localhost",
                String.valueOf(fakeFtpServer.getServerControlPort()),
                localFilePath,
                "foobar.txt",
                "user",
                "password",
                "ascii",
                "",
                "");

        assertEquals(result.get(RETURN_RESULT), SUCCESS_RESULT);
        assertTrue(fileSystem.exists("/data/foobar.txt"));
    }


    @Test
    public void executeWithInexistentLocalFile() throws Exception {


        Map<String, String> result = putOperation.execute("localhost",
                String.valueOf(fakeFtpServer.getServerControlPort()),
                "noFile.txt",
                "foobar.txt",
                "user",
                "password",
                "ascii",
                "",
                "");

        assertEquals(result.get(RETURN_RESULT), String.format(EXCEPTION_INVALID_LOCAL_FILE, "noFile.txt"));
        assertFalse(fileSystem.exists("/data/foobar.txt"));
    }



}