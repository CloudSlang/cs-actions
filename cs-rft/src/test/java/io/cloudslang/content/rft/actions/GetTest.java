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



import io.cloudslang.content.rft.actions.ftp.Get;
import io.cloudslang.content.rft.services.FTPService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.File;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.rft.utils.Constants.*;
import static org.junit.Assert.*;


public class GetTest {
    Get getOperation;
    FakeFtpServer fakeFtpServer;
    FTPService ftpService;

    @Before
    public void setUp() throws Exception {

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new FileEntry("/data/foobar.txt","abdef 1234567890"));

        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(0);
        fakeFtpServer.addUserAccount(new UserAccount("user","password","/data"));
        fakeFtpServer.start();

        getOperation  = new Get();
        ftpService  = new FTPService();

    }

    @Test
    public void executeWithSuccess() {
        Map<String,String> result = getOperation.execute("localhost",
                String.valueOf(fakeFtpServer.getServerControlPort()),
                "downloaded.txt",
                "foobar.txt",
                "user",
                "password",
                "ascii",
                "",
                "");
        assertEquals(result.get(RETURN_RESULT),SUCCESS_RESULT);

        File file  = new File("downloaded.txt");
        assertTrue(file.delete());
    }


    @Test
    public void executeWithInvalidHostname() {

        Map<String,String> result = getOperation.execute("nohost",
                "55555",
                "downloaded.txt",
                "invalidFile.txt",
                "user",
                "password",
                "ascii",
                "",
                "");
        assertEquals(result.get(RETURN_RESULT),String.format(EXCEPTION_UNKNOWN_HOST,"nohost"));
    }

    @Test
    public void executeWithInvalidPort() {

        Map<String,String> result = getOperation.execute("localhost",
                "55555",
                "downloaded.txt",
                "invalidFile.txt",
                "user",
                "password",
                "ascii",
                "",
                "");
        assertTrue(result.get(RETURN_RESULT).contains("Could not connect to "));
    }

    @Test
    public void executeWithInvalidRemoteFile() {

        String invalidRemoteFile="invalidFile.txt";
        Map<String,String> result = getOperation.execute("localhost",
                String.valueOf(fakeFtpServer.getServerControlPort()),
                "localFile.txt",
                invalidRemoteFile,
                "user",
                "password",
                "ascii",
                "",
                "");
        assertEquals(result.get(RETURN_RESULT),String.format(EXCEPTION_INVALID_REMOTE_FILE,invalidRemoteFile));
    }

    @Test
    public void executeWithWrongUser() {
        Map<String,String> result = getOperation.execute("localhost",
                String.valueOf(fakeFtpServer.getServerControlPort()),
                "downloaded.txt",
                "foobar.txt",
                "usser",
                "password",
                "ascii",
                "",
                "");
        assertNotEquals(result.get(RETURN_RESULT),SUCCESS_RESULT);

    }

    @Test
    public void executeWithIncorrectPassword() {
        Map<String,String> result = getOperation.execute("localhost",
                String.valueOf(fakeFtpServer.getServerControlPort()),
                "downloaded.txt",
                "foobar.txt",
                "user",
                "passwesdaasfa",
                "ascii",
                "",
                "");
        assertNotEquals(result.get(RETURN_RESULT),SUCCESS_RESULT);

    }

    @After
    public void teardown() throws Exception  {
        fakeFtpServer.stop();
    }
}