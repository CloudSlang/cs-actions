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
package io.cloudslang.content.rft.utils.ftp;

public final class Constants {
    public static final String BOOLEAN_FALSE = "false";
    public static final String BINARY_FILE_TYPE = "binary";
    public static final String ASCII_FILE_TYPE = "ascii";
    public static final String ANONYMOUS = "anonymous";
    public static final String PORT_21 = "21";
    public static final String CHARACTER_SET_LATIN1 = "ISO-8859-1";

    public static final String NEW_LINE = "\n";

    public static final String FTP_REPLY_CODE = "ftpReplyCode";
    public static final String FTP_SESSION_LOG = "ftpSessionLog";

    public static final String SUCCESS_RESULT = "The operation was successfully completed!";

    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_TYPE = "The '%s' for %s input is not a valid type value.(ascii/binary)";
    public static final String EXCEPTION_INVALID_PORT = "Invalid port number: %s. Valid values: 0-65535";
    public static final String EXCEPTION_UNKNOWN_HOST = "Unknown host: %s.";
    public static final String EXCEPTION_INVALID_LOCAL_FILE = "The value '%s'  is not a valid file path.";
    public static final String EXCEPTION_INVALID_REMOTE_FILE = "'%s' does not exist.";
    public static final String EXCEPTION_CONNECT = "Could not connect to %s : %s, reason: '%s'";
    public static final String EXCEPTION_LOGIN = "Username or password is incorrect";
    public static final String EXCEPTION_CHARACTER_SET = "The character set was invalid!";
}