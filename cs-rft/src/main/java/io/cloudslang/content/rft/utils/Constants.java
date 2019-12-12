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

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class Constants {
    public static final String EMPTY_STRING = "";
    public static final String NO_ACK_RECEIVED = "No ack received";
    public static final String SSH_SESSIONS_DEFAULT_ID = "sshSessions:default-id";
    public static final String STDOUT = "STDOUT";
    public static final String STDERR = "STDERR";

    // default values
    public static final int DEFAULT_PORT = 22;
    public static final int DEFAULT_TIMEOUT = 90000;
    public static final String DEFAULT_KNOWN_HOSTS_POLICY = "strict";
    public static final Path DEFAULT_KNOWN_HOSTS_PATH = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");
    public static final int DEFAULT_PROXY_PORT = 8080;


    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String BINARY_FILE_TYPE = "binary";
    public static final String ASCII_FILE_TYPE = "ascii";
    public static final String PORT_21 = "21";
    public static final String CHARACTER_SET_LATIN1 = "ISO-8859-1";
    public static final String CHARACTER_SET_UTF8 = "UTF-8";

    public static final String NEW_LINE = "\n";

    public static final String FTP_REPLY_CODE = "ftpReplyCode";
    public static final String FTP_SESSION_LOG = "ftpSessionLog";
    public static final String FILES = "files";
    public static final String FOLDERS = "folders";

    public static final String SUCCESS_RESULT = "The operation was successfully completed!";

    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_TYPE = "The '%s' for %s input is not a valid type value.(ascii/binary)";
    public static final String EXCEPTION_INVALID_PORT = "Invalid port number: %s. Valid values: 0-65535";
    public static final String EXCEPTION_UNKNOWN_HOST = "Unknown host: %s.";
    public static final String EXCEPTION_INVALID_LOCAL_FILE = "The value '%s'  is not a valid file path.";
    public static final String EXCEPTION_INVALID_REMOTE_FILE = "'%s' does not exist.";
    public static final String EXCEPTION_CONNECT = "Could not connect to %s : %s, reason: '%s'";
    public static final String EXCEPTION_CHARACTER_SET = "The character set was invalid!";
    public static final String EXCEPTION_CONNECTION = "Could not connect to %s : %s";
    public static final String EXCEPTION_LOCAL_FILE_EXISTS = "File '%s' already exists!";
    public static final String EXCEPTION_UNABLE_TO_RETRIEVE = "Unable to retrieve file over SFTP";
    public static final String EXCEPTION_UNABLE_TO_STORE = "Unable to store file over SFTP";
    public static final String EXCEPTION_UNABLE_SAVE_SESSION = "The SSH Session could not be saved in the given sessionParam";


}