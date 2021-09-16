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
package io.cloudslang.content.rft.utils;

public class Descriptions {
    public static class FTPDescriptions {

        public static final String HOSTNAME_DESC = "Hostname or ip address of ftp server.";
        public static final String PORT_DESC = "The port the ftp service is listening on.";
        public static final String LOCAL_FILE_DESC = "The local file name.";
        public static final String REMOTE_FILE_DESC = "The remote file.";
        public static final String USER_DESC = "The user to connect as.";
        public static final String PASSWORD_DESC = "The password for user.";
        public static final String TYPE_DESC = "The type of the file to get (binary or ascii).";
        public static final String PASSIVE_DESC = "If true, passive connection mode will be enabled.  The default is " +
                "active connection mode.";
        public static final String CHARACTER_SET_DESC = "The name of the control encoding to use. Default is ISO-8859-1" +
                " (Latin-1).";

        public static final String FAILURE_DESC = "There was an error during the execution.";
        public static final String SUCCESS_DESC = "The operation was successfully executed.";

        public static final String FTP_REPLY_CODE_DESC = "The ftp reply code.";
        public static final String FTP_SESSION_LOG_DESC = "Log of ftp commands.";
        public static final String RETURN_RESULT_DESC = "A message is returned in case of success, an error message is" +
                " returned in case of failure.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the" +
                " operation.";
    }

    public static class SFTPDescriptions{
        public static final String HOST_NAME = "IP address/host name.";
        public static final String PORT_DESC = "The port to connect to on host.";
        public static final String USERNAME_DESC = "Remote username.";
        public static final String PASSWORD_DESC = "Password to authenticate. If using a private key file this will be " +
                "used as the passphrase for the file";
        public static final String PROXY_HOST_DESC = "The proxy server used to access the remote host.";
        public static final String PROXY_PORT_DESC = "The proxy server port.";
        public static final String PROXY_USERNAME_DESC = "The username used when connecting to the proxy.";
        public static final String PROXY_PASSWORD_DESC = "The password used when connecting to the proxy.";
        public static final String CONNECTION_TIMEOUT_DESC = "Time in seconds to wait for the connection to complete.";
        public static final String EXECUTION_TIMEOUT_DESC = "Time in seconds to wait for the operation to complete.";
        public static final String PRIVATE_KEY_DESC = "Absolute path for private key file for public/private key " +
                "authentication.";
        public static final String REMOTE_FILE_DESC = "The remote file.";
        public static final String REMOTE_LOCATION_DESC = "The remote location where the file is to be placed.";
        public static final String LOCAL_FILE_DESC = "The path to the file on the RAS to be copied remotely using SFTP.";
        public static final String LOCAL_LOCATION_DESC = "The location where file is to be placed on the RAS.";
        public static final String GLOBAL_SESSION_DESC = "The sessionObject that holds the connection if the close " +
                "session is false.";
        public static final String DELIMITER_DESC = "A delimiter to use for the result lists (returnResult, files, " +
                "folders).";
        public static final String REMOTE_PATH_DESC = "The remote file or directory name.";

        public static final String CHARACTER_SET_DESC = "The name of the control encoding to use. Examples: UTF-8, " +
                "EUC-JP, SJIS.  Default is UTF-8.";
        public static final String CLOSE_SESSION_DESC = "Close the SSH session at completion of operation?  Default " +
                "value is true.  If false the SSH session can be reused by other SFTP commands in the same flow.  " +
                "Valid values: true, false.";

        public static final String SUCCESS_DESC = "Command completed successfully.";
        public static final String FAILURE_DESC = "Command failed.";

        public static final String RETURN_RESULT_DESC = "Remote file will be copied to local system.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the" +
                " operation.";
        public static final String FILES_DESC = "A list of files in the remote directory.";
        public static final String FOLDERS_DESC = "A list of folders in the remote directory.";
    }

    public static class SFTPCommandDescriptions{
        public static final String SUCCESS_DESC = "Command completed successfully.";
        public static final String FAILURE_DESC = "Command failed.";

        public static final String RETURN_RESULT_DESC = "Command was executed successfully.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the " +
                "operation.";

        public static final String MODE_DESC = "New numeric mode for remote file or directory.  First digit is for" +
                " everyone, second digit is for group, third digit is for owner.  Setuid, Setgid, and Sticky bits are" +
                " not supported via SFTP.  Examples: 765 (rwx,rw-,r-x), 432 (r--,-wx,-w-), 100 (--x,---,---).";
        public static final String UID_DESC = "Numeric user id of the new owner for the remote file or directory.";
        public static final String GID_DESC = "New numeric group id for remote file or directory.";
        public static final String COMMAND_TYPE_DESC = "The command type";
    }
}
