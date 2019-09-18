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
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";

    }

    public static final class FTPInputs{
        public static final String PARAM_HOSTNAME = "hostName";
        public static final String PARAM_PORT = "port";
        public static final String PARAM_LOCAL_FILE = "localFile";
        public static final String PARAM_REMOTE_FILE = "remoteFile";
        public static final String PARAM_USER = "user";
        public static final String PARAM_PASSWORD = "password";
        public static final String PARAM_TYPE = "type";
        public static final String PARAM_PASSIVE = "passive";
        public static final String PARAM_CHARACTER_SET = "characterSet";
    }

    public static final class SFTPInputs {

        //COMMON INPUTS
        public static final String PARAM_HOST = "host";
        public static final String PARAM_PORT = "port";
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";
        public static final String PARAM_PRIVATE_KEY = "privateKey";
        public static final String PARAM_CHARACTER_SET = "characterSet";
        public static final String PARAM_CLOSE_SESSION = "closeSession";
        public static final String PARAM_AGENT_FORWARDING = "agentForwarding";


        //GET ONLY
        public static final String PARAM_REMOTE_FILE = "remoteFile";
        public static final String PARAM_LOCAL_LOCATION = "localLocation";

        //PUT ONLY
        public static final String PARAM_REMOTE_LOCATION = "remoteLocation";
        public static final String PARAM_LOCAL_FILE = "localFile";

        //GET CHILDREN ONLY
        public static final String PARAM_DELIMITER = "delimiter";
        public static final String PARAM_REMOTE_PATH = "remotePath";

    }

}
