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

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {

    public static final class RemoteSecureCopyInputs {
        public static final String SOURCE_HOST = "sourceHost";
        public static final String SOURCE_PATH = "sourcePath";
        public static final String SOURCE_PORT = "sourcePort";
        public static final String SOURCE_PRIVATE_KEY_FILE = "sourcePrivateKeyFile";
        public static final String SOURCE_USERNAME = "sourceUsername";
        public static final String SOURCE_PASSWORD = "sourcePassword";
        public static final String DESTINATION_HOST = "destinationHost";
        public static final String DESTINATION_PATH = "destinationPath";
        public static final String DESTINATION_PORT = "destinationPort";
        public static final String DESTINATION_PRIVATE_KEY_FILE = "destinationPrivateKeyFile";
        public static final String DESTINATION_USERNAME = "destinationUsername";
        public static final String DESTINATION_PASSWORD = "destinationPassword";
        public static final String KNOWN_HOSTS_POLICY = "knownHostsPolicy";
        public static final String KNOWN_HOSTS_PATH = "knownHostsPath";
        public static final String TIMEOUT = "timeout";

    }

    public static final class FTPInputs {
        public static final String HOST_NAME = "hostName";
        public static final String PORT = "port";
        public static final String LOCAL_FILE = "localFile";
        public static final String REMOTE_FILE = "remoteFile";
        public static final String USER = "user";
        public static final String PASSWORD = "password";
        public static final String TYPE = "type";
        public static final String PASSIVE = "passive";
        public static final String CHARACTER_SET = "characterSet";
    }

    public static final class SFTPInputs {

        //COMMON INPUTS
        public static final String HOST = "host";
        public static final String PORT = "port";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";

        public static final String PRIVATE_KEY = "privateKey";
        public static final String CHARACTER_SET = "characterSet";
        public static final String CLOSE_SESSION = "closeSession";

        //GET ONLY
        public static final String REMOTE_FILE = "remoteFile";
        public static final String NEW_REMOTE_FILE = "newRemoteFile";
        public static final String LOCAL_LOCATION = "localLocation";
        public static final String REMOTE_LOCATION = "remotePath";
        public static final String LOCAL_LOCATION = "localPath";

        //PUT ONLY
        public static final String REMOTE_PATH_PUT = "remotePath";
        public static final String LOCAL_PATH_PUT = "localPath";
        public static final String LOCAL_FILE = "localFile";

        //GET CHILDREN ONLY
        public static final String DELIMITER = "delimiter";
        public static final String REMOTE_PATH = "remotePath";
        public static final String NEW_REMOTE_PATH = "newRemotePath";

        //SFTP COMMAND
        public static final String MODE = "mode";
        public static final String COMMAND_TYPE = "commandType";
        public static final String GID = "gid";
        public static final String UID = "uid";

    }

    public static final class RemoteCopyInputs {
        public static final String SRC_HOST = "sourceHost";
        public static final String SRC_PORT = "sourcePort";
        public static final String SRC_USERNAME = "sourceUsername";
        public static final String SRC_PASSWORD = "sourcePassword";
        public static final String SRC_PRIVATE_KEY_FILE = "sourcePrivateKeyFile";
        public static final String SRC_PATH = "sourcePath";
        public static final String SRC_PROTOCOL = "sourceProtocol";
        public static final String SRC_CHARACTER_SET = "sourceCharacterSet";
        public static final String DEST_HOST = "destinationHost";
        public static final String DEST_PORT = "destinationPort";
        public static final String DEST_USERNAME = "destinationUsername";
        public static final String DEST_PASSWORD = "destinationPassword";
        public static final String DEST_PRIVATE_KEY_FILE = "destinationPrivateKeyFile";
        public static final String DEST_PATH = "destinationPath";
        public static final String DEST_PROTOCOL = "destinationProtocol";
        public static final String DEST_CHARACTER_SET = "destinationCharacterSet";
    }

    public static final class CommonInputs {
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String CONNECTION_TIMEOUT = "connectionTimeout";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
    }

    public static final class SCPInputs {
        public static final String KNOWN_HOSTS_POLICY = "known_hosts_policy";
        public static final String KNOWN_HOSTS_PATH = "known_hosts_path";

        //SCP Copy file only
        public static final String HOST = "host";
        public static final String PORT = "port";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOCAL_FILE = "localFile";
        public static final String COPY_ACTION = "copyAction";
        public static final String REMOTE_FILE = "remoteFile";
        public static final String PRIVATE_KEY = "privateKey";


        //SCP Remote Copy File only
        public static final String SOURCE_PATH = "sourcePath";
        public static final String SOURCE_HOST = "sourceHost";
        public static final String SOURCE_PORT = "sourcePort";
        public static final String SOURCE_USERNAME = "sourceUsername";
        public static final String SOURCE_PASSWORD = "sourcePassword";
        public static final String SOURCE_PRIVATE_KEY = "sourcePrivateKey";
        public static final String DESTINATION_PATH = "destinationPath";
        public static final String DESTINATION_HOST = "destinationHost";
        public static final String DESTINATION_PORT = "destinationPort";
        public static final String DESTINATION_USERNAME = "destinationUsername";
        public static final String DESTINATION_PASSWORD = "destinationPassword";
        public static final String DESTINATION_PRIVATE_KEY = "destinationPrivateKey";
    }
}
