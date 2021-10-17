/*
 * (c) Copyright 2021 Micro Focus
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
        public static final String CHARACTER_SET_DESC = "The name of the control encoding to use.\n" +
                " Default is ISO-8859-1 (Latin-1).";

        public static final String FAILURE_DESC = "There was an error during the execution.";
        public static final String SUCCESS_DESC = "The operation was successfully executed.";

        public static final String FTP_REPLY_CODE_DESC = "The ftp reply code.";
        public static final String FTP_SESSION_LOG_DESC = "Log of ftp commands.";
        public static final String RETURN_RESULT_DESC = "A message is returned in case of success, an error message is " +
                "returned in case of failure.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the operation.";
    }

    public static class SFTPDescriptions {
        public static final String HOST_NAME = "IP address/host name.";
        public static final String PORT_DESC = "The port to connect to on host.";
        public static final String USERNAME_DESC = "Remote username.";
        public static final String PASSWORD_DESC = "Password to authenticate. If using a private key file this will be " +
                "used as the passphrase for the file";
        public static final String PRIVATE_KEY_DESC = "Absolute path for private key file for public/private key authentication.";
        public static final String GLOBAL_SESSION_DESC = "The sessionObject that holds the connection if the close session is false.";
        public static final String DELIMITER_DESC = "A delimiter to use for the result lists (returnResult, files, folders).";
        public static final String REMOTE_PATH_DESC = "The full path of the remote file or directory name.";
        public static final String NEW_REMOTE_PATH_DESC = "The new name for the remote file or directory.";

        public static final String CHARACTER_SET_DESC = "The name of the control encoding to use.\n" +
                "Examples: UTF-8, EUC-JP,SJIS.  \n" +
                "Default is UTF-8.";
        public static final String CLOSE_SESSION_DESC = "Close the SSH session at completion of operation." +
                "Default value is true.  If false the SSH session can be reused by other SFTP commands in the same flow.\n  " +
                "Valid values: true, false.";

        public static final String RETURN_RESULT_DESC = "Remote file will be copied to local system.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the operation.";
        public static final String FILES_DESC = "A list of files in the remote directory.";
        public static final String FOLDERS_DESC = "A list of folders in the remote directory.";
    }

    public static class RemoteCopyDescriptions {
        public static final String SRC_HOST_DESC = "The host where the source file is located.";
        public static final String SRC_PORT_DESC = "The port for connecting to the source host.";
        public static final String SRC_USERNAME_DESC = "The username for connecting to the host of the source file.";
        public static final String SRC_PASSWORD_DESC = "The password for connecting to the host of the source file.";
        public static final String SRC_PRIVATE_KEY_FILE_DESC = "Absolute path of the private key file for public/private" +
                " key authentication on the source host.";
        public static final String SRC_PATH_DESC = "The absolute path to the source file. When using SMB3 the destination path should " +
                "start with the samba shared folder. For more details about how to configure the samba shared folder check " +
                "the usage section from the CP's release notes.";
        public static final String SRC_PROTOCOL_DESC = "The protocol used to copy from the source file. \n" +
                "Valid values: local, SCP, SFTP, SMB3.";
        public static final String SRC_CHARACTER_SET_DESC = "The name of the control encoding to use with source host " +
                "for SFTP protocol. \n" +
                "Examples: UTF-8, EUC-JP, SJIS.";
        public static final String DEST_HOST_DESC = "The destination host of the transferred file.";
        public static final String DEST_PORT_DESC = "The port for connecting to the destination host.";
        public static final String DEST_USERNAME_DESC = "The username for connecting to the destination host.";
        public static final String DEST_PASSWORD_DESC = "The password for connecting to the destination host.";
        public static final String DEST_PRIVATE_KEY_FILE_DESC = "Absolute path of the private key file for public/private" +
                " key authentication on the destination host.";
        public static final String DEST_PATH_DESC = "The absolute path to the destination file. When using the SMB3 protocol " +
                "the destination path should start with the samba shared folder.\n" +
                "For more details about how to configure the samba shared folder check the usage section from the CP's " +
                "release notes. \n" +
                "Example: sambaSharedFolder\\folder1\\folder2\\file.";
        public static final String DEST_PROTOCOL_DESC = "The protocol used to copy to the destination file.\n" +
                "Valid values: local, SCP, SFTP, SMB3.";
        public static final String DEST_CHARACTER_SET_DESC = "The name of the control encoding to use with destination host " +
                "for SFTP protocol. \n" +
                "Examples: UTF-8, EUC-JP, SJIS.   \n" +
                "Default: UTF-8.";
        public static final String RETURN_RESULT_REMOTE_COPY_DESC = " This is the primary output and it contains the " +
                "success message if the operation successfully completes, or an exception message otherwise.";
        public static final String REMOTE_COPY_ACTION_DESC = "This operation copies files between two remote machines " +
                "using different protocols (local, SCP, SFTP, SMB3).";

    }

    public static class CommonInputsDescriptions {
        public static final String PROXY_HOST_DESC = "The proxy server used to access the remote host.";
        public static final String PROXY_PORT_DESC = "The proxy server port.";
        public static final String PROXY_USERNAME_DESC = "The username used when connecting to the proxy.";
        public static final String PROXY_PASSWORD_DESC = "The password used when connecting to the proxy.";
        public static final String CONNECTION_TIMEOUT_DESC = "Time in seconds to wait for the connection to complete.";
        public static final String EXECUTION_TIMEOUT_DESC = "Time in seconds to wait for the operation to complete.";
        public static final String PASSIVE_DESC = "If true, passive connection mode will be enabled.  The default is " +
                "active connection mode.";
        public static final String TYPE_DESC = "The type of the file to get (binary or ascii).";
        public static final String SUCCESS_DESC = "The file was copied successfully.";
        public static final String FAILURE_DESC = "The file could not be copied.";
        public static final String RETURN_RESULT_DESC = " This is the primary output and it contains the " +
                "success message if the operation successfully completes, or an exception message otherwise.";
    }

    public static class SCPDescriptions {

        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the operation.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String RETURN_RESULT_DESC = " This is the primary output and it contains the " +
                "success message if the operation successfully completes, or an exception message otherwise.";

        public static final String SUCCESS_DESC = "The file was copied successfully.";
        public static final String FAILURE_DESC = "The file could not be copied.";
        public static final String KNOWN_HOSTS_POLICY_DESC = "Optional - Policy used for managing known_hosts file.\n " +
                "Valid: 'allow', 'strict', 'add'\n" +
                "Default: 'allow'";
        public static final String KNOWN_HOSTS_PATH_DESC = "Path to the known_hosts file.";

        //SCP Copy File
        public static final String SCP_COPY_FILE_ACTION_DESC = "Copy files to and from remote machine through SSH";
        public static final String HOST_DESC = "IP address/host name.";
        public static final String PORT_DESC = "The port number to connect to.";
        public static final String USERNAME_DESC = "Remote username.";
        public static final String PASSWORD_DESC = "Password of user. If using a private key file this will be used as " +
                "the passphrase for the file.";
        public static final String LOCAL_FILE_DESC = "Absolute path to the local file. This path is relative to the host" +
                " that the operation is running on.";
        public static final String COPY_ACTION_DESC = "To/From copy action.\n Valid values: to, from";
        public static final String REMOTE_FILE_DESC = "Absolute path to remote file.";
        public static final String PRIVATE_KEY_DESC = "Absolute path for private key file for public/private key authentication.";

        //SCP Remote Copy File
        public static final String SCP_REMOTE_COPY_FILE_ACTION_DESC = "Copies files between two remote machines through secure SSH.";
        public static final String SOURCE_PATH_DESC = "The absolute path to the source file.";
        public static final String SOURCE_HOST_DESC = "The host of the source file.";
        public static final String SOURCE_PORT_DESC = "The port used for connecting to the source host.";
        public static final String SOURCE_USERNAME_DESC = "The username for connecting to the host of the source file.";
        public static final String SOURCE_PASSWORD_DESC = "The password for connecting to the host of the source file. " +
                "If using a private key file this will be used as the passphrase for the file.";
        public static final String SOURCE_PRIVATE_KEY_DESC = "Absolute path of the private key file for public/private " +
                "key authentication on the source host.";
        public static final String DESTINATION_PATH_DESC = "The absolute path to the destination file.";
        public static final String DESTINATION_HOST_DESC = "The host of the destination file.";
        public static final String DESTINATION_PORT_DESC = "The port used for connecting to the destination host.";
        public static final String DESTINATION_USERNAME_DESC = "The username for connecting to the host of the destination" +
                " file.";
        public static final String DESTINATION_PASSWORD_DESC = "The password for connecting to the host of the destination" +
                " file. If using a private key file this will be used as the passphrase for the file.";
        public static final String DESTINATION_PRIVATE_KEY_DESC = "Absolute path of the private key file for " +
                "public/private key authentication on the destination host.";

    }

    public static class SFTPCommandDescriptions {
        public static final String SUCCESS_DESC = "Command completed successfully.";
        public static final String FAILURE_DESC = "Command failed.";

        public static final String MODE_DESC = "New numeric mode for remote file or directory.  First digit is for" +
                " everyone, second digit is for group, third digit is for owner.  Setuid, Setgid, and Sticky bits are" +
                " not supported via SFTP.  Examples: 765 (rwx,rw-,r-x), 432 (r--,-wx,-w-), 100 (--x,---,---).";
        public static final String UID_DESC = "Numeric user id of the new owner for the remote file or directory.";
        public static final String GID_DESC = "New numeric group id for remote file or directory.";
        public static final String COMMAND_TYPE_DESC = "The command type";
    }

    public static class SFTPDeleteFileDescriptions {

        public static final String REMOTE_PATH_DELETE_FILE_DESC = "The path to the remote file that will be deleted.\n" +
                "Examples: C:/Users/Administrator, root/testFolder";
        public static final String REMOTE_FILE_DELETE_DESC = "The name and extension of the file that will be deleted.\n" +
                "Examples: file.txt";
        public static final String SUCCESS_DESC = "File was deleted successfully.";
        public static final String FAILURE_DESC = "The file could not be deleted.";

    }

    public static class SFTPGetChildrenDescriptions {
        public static final String REMOTE_PATH_GET_CHILDREN_DESC = "The full path of directory from where the list of" +
                "subdirectories and files is retrieved.\n" +
                "Example: C:/Users/Administrator/testDir, root/testDir";
    }

    public static class SFTPCreateDirectoryDescriptions {

        public static final String REMOTE_PATH_CREATE_DESC = "The path where the new directory will be created.\n" +
                "Examples: C:/Users/Administrator/newFolder, root/test/newFolder";
        public static final String CREATE_DIR_SUCCESS_DESC = "Directory was created successfully.";
        public static final String SUCCESS_DESC = "The directory was created successfully.";
        public static final String FAILURE_DESC = "The directory could not be created.";

    }

    public static class SFTPDeleteDirectoryDescriptions {

        public static final String REMOTE_PATH_DELETE_DESC = "The absolute path to the directory to be deleted.\n" +
                "Examples: C:/Users/Administrator/folder, root/test/folder";
        public static final String DELETE_DIR_SUCCESS_DESC = "Directory was deleted successfully.";
        public static final String FAILURE_DELETE_DIR_DESC = "The directory could not be deleted.";

    }

    public static class SFTPRenameDescriptions {

        public static final String RENAME_REMOTE_PATH_DESC = "The path to the file or directory to be renamed.\n" +
                "Example: C:/Users/Administrator, root/test";
        public static final String RENAME_REMOTE_FILE_DESC = "The name of the file or directory that will be renamed.\n" +
                "Example: oldFileName.txt, oldFoladerName";
        public static final String RENAME_NEW_REMOTE_PATH_DESC = "The path to the new file or directory.\n";
        public static final String NEW_REMOTE_FILE_DESC = "The new file or directory name.\n" +
                "Example: newFileName.txt, newFolderName";
        public static final String RENAME_SUCCESS_DESC = "Remote file or directory was renamed.";
        public static final String RENAME_FAILURE_DESC = "Remote file or directory couldn't be renamed.";

    }

    public static class SFTPUploadFileDescriptions {

        public static final String REMOTE_LOCATION_DESC = "The remote directory path where the file will be uploaded. \n" +
                "Examples: C:/Users/Administrator, root/test";
        public static final String LOCAL_LOCATION_DESC = "The local directory path of the file that will be copied. \n" +
                "Examples: C:/Users/Administrator, root/test";
        public static final String LOCAL_FILE_DESC = "The file to be copied remotely using SFTP. \n" +
                "Example: test.txt";

    }

    public static class SFTPDownloadFileDescriptions {

        public static final String REMOTE_FILE_DESC = "The file to be downloaded remotely using SFTP. \n" +
                "Example: test.txt";
        public static final String REMOTE_LOCATION_DESC = "The remote directory path from where the file will be downloaded. \n" +
                "Examples: C:/Users/Administrator, root/test";
        public static final String LOCAL_LOCATION_DESC = "The local directory path where the file will be downloaded. \n" +
                "Examples: C:/Users/Administrator, root/test";

    }
}
