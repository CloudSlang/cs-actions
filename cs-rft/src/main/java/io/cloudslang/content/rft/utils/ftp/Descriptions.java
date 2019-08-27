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

public class Descriptions {
    public static class Common {

        public static final String PARAM_HOSTNAME_DESC = "Hostname or ip address of ftp server.";
        public static final String PARAM_PORT_DESC = "The port the ftp service is listening on.";
        public static final String PARAM_LOCAL_FILE_DESC = "The local file name.";
        public static final String PARAM_REMOTE_FILE_DESC = "The remote file.";
        public static final String PARAM_USER_DESC = "The user to connect as.";
        public static final String PARAM_PASSWORD_DESC = "The password for user.";
        public static final String PARAM_TYPE_DESC = "The type of the file to get (binary or ascii).";
        public static final String PARAM_PASSIVE_DESC = "If true, passive connection mode will be enabled.  The default is active connection mode.";
        public static final String PARAM_CHARACTER_SET_DESC = "The name of the control encoding to use. Default is ISO-8859-1 (Latin-1).";

        public static final String FAILURE_DESC = "There was an error during the execution.";
        public static final String SUCCESS_DESC = "The operation was successfully executed.";

        public static final String FTP_REPLY_CODE_DESC = "The ftp reply code.";
        public static final String FTP_SESSION_LOG_DESC = "Log of ftp commands.";
        public static final String RETURN_RESULT_DESC = "A message is returned in case of success, an error message is returned in case of failure.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the operation.";
    }
}
