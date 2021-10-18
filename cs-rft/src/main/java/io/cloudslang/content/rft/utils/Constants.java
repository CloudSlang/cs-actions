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

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

    //operation name
    public static final String SFTP_COMMAND = "SFTP Command";
    public static final String SFTP_DELETE_FILE = "SFTP Delete File";
    public static final String SFTP_DOWNLOAD_FILE = "SFTP Download File";
    public static final String SFTP_GET_CHILDREN = "SFTP Get Children Operation";
    public static final String SFTP_DELETE_FILE_DESCRIPTION = "Deletes a file remotely using Secure FTP (SFTP)";
    public static final String SFTP_CREATE_DIRECTORY = "SFTP Create Directory";
    public static final String SFTP_CREATE_DIRECTORY_DESCRIPTION = "Creates a directory remotely using Secure FTP (SFTP).";
    public static final String SFTP_DELETE_DIRECTORY = "SFTP Delete Directory";
    public static final String SFTP_DELETE_DIRECTORY_DESCRIPTION = "Deletes a directory remotely using Secure FTP (SFTP).";
    public static final String SFTP_RENAME = "SFTP Rename";
    public static final String SFTP_RENAME_DESCRIPTION = "Renames a file or directory remotely using Secure FTP (SFTP).";

    public static final String SSH_SESSION = "sshSession:";
    public static final String EMPTY_STRING = "";
    public static final String HYPHEN = "-";
    public static final String BACKSLASH = "/";
    public static final String NO_ACK_RECEIVED = "No ack received";
    public static final String SSH_SESSIONS_DEFAULT_ID = "sshSessions:default-id";

    // default values
    public static final int DEFAULT_PORT = 22;
    public static final int DEFAULT_TIMEOUT = 90000;
    public static final String DEFAULT_KNOWN_HOSTS_POLICY = "allow";
    public static final Path DEFAULT_KNOWN_HOSTS_PATH = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");
    public static final int DEFAULT_PROXY_PORT = 8080;
    public static final String DEFAULT_DELIMITER = String.valueOf(',');
    public static final String SUCCESS_RETURN_CODE = "0";
    public static final String FAILURE_RETURN_CODE = "-1";


    public static final String VERSION = "v2";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String BINARY_FILE_TYPE = "binary";
    public static final String ASCII_FILE_TYPE = "ascii";
    public static final String PORT_21 = "21";
    public static final String CHARACTER_SET_LATIN1 = "ISO-8859-1";
    public static final String CHARACTER_SET_UTF8 = "UTF-8";
    public static final String DEFAULT_CONNECTION_TIMEOUT = "60";
    public static final String DEFAULT_EXECUTION_TIMEOUT = "60";

    public static final String NEW_LINE = "\n";
    public static final String LOCALHOST = "localhost";

    public static final String FTP_REPLY_CODE = "ftpReplyCode";
    public static final String FTP_SESSION_LOG = "ftpSessionLog";
    public static final String FILES = "files";
    public static final String FOLDERS = "folders";

    public static final String CHMOD = "chmod";
    public static final String CHGRP = "chgrp";
    public static final String CHOWN = "chown";
    public static final String RENAME = "rename";
    public static final String SFTP_COMMON_INPUTS = "sfptCommonInputs";
    public static final String FAILURE = "failure";

    //SFTP COPIER
    public static final String SFTP = "sftp";
    public static final String NULL_OUTPUT = "LS Output was null.";

    public static final String SUCCESS_RESULT = "The file transfer was successfully completed!";

    public static final String EXCEPTION_NULL_EMPTY = "The %s input value can't be null or empty.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s value for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_TYPE = "The '%s' value for %s input is not a valid type value.(ascii/binary)";
    public static final String EXCEPTION_INVALID_PORT = "Invalid port number: %s. The valid value is between: 0-65535.";
    public static final String EXCEPTION_INVALID_PROTOCOL = "Invalid protocol value: %s. The valid values are: local, " +
            "scp, sftp, smb3.";
    public static final String EXCEPTION_INVALID_KNOWN_HOSTS_POLICIES = "%s is an invalid value for known host policy. The valid values are: allow, strict, add.";
    public static final String EXCEPTION_UNKNOWN_HOST = "Unknown host: %s.";
    public static final String EXCEPTION_INVALID_LOCAL_FILE = "The value '%s'  is not a valid file path.";
    public static final String EXCEPTION_INVALID_REMOTE_FILE = "'%s' does not exist.";
    public static final String EXCEPTION_CONNECT = "Could not connect to %s : %s, reason: '%s'.";
    public static final String EXCEPTION_CHARACTER_SET = "The value of the character set input is invalid!";
    public static final String EXCEPTION_LOCAL_FILE_EXISTS = "File '%s' already exists!";
    public static final String EXCEPTION_UNABLE_TO_RETRIEVE = "Unable to retrieve file over SFTP.";
    public static final String EXCEPTION_UNABLE_TO_STORE = "Unable to store file over SFTP.";
    public static final String EXCEPTION_UNABLE_SAVE_SESSION = "The SSH Session could not be saved in the given sessionParam.";
    public static final String EXCEPTION_INVALID_NUMBER = "%s for %s input is not a valid number value.";
    public static final String EXCEPTION_INVALID_NEGATIVE_NUMBER = "%s for %s input should not be a negative number.";
    public static final String EXCEPTION_INVALID_COPY_ACTION= "%s is not a valid value for the input copy_action, valid values for are: to, from.";
    public static final String EXCEPTION_EXECUTION_TIMED_OUT = "Operation timed out.";
    public static final String EXCEPTION_EXISTING_DIRECTORY = "Failed to create directory. Directory may already exist" +
            " or the path to the new folder is invalid.";
    public static final String EXCEPTION_DIRECTORY_DELETE = "Failed to delete directory. Directory may not be empty.";
    public static final String EXCEPTION_LOCAL_HOST = "When the protocol is local, the host must be localhost!\\n";
    public static final String EXCEPTION_REMOTE_PATH_FILE = "The %s and %s inputs can't be both empty. A value must be provided for at least one.";
}