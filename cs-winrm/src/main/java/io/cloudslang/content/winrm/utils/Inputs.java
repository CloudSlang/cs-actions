/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.winrm.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {
    public static class WinRMInputs{
        public static final String HOST = "host";
        public static final String PORT ="port";
        public static final String PROTOCOL = "protocol";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String COMMAND = "command";
        public static final String AUTH_TYPE = "authType";
        public static final String PROXY_HOST ="proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME ="proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String TLS_VERSION = "tlsVersion";
        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String KEYSTORE = "keystore";
        public static final String KEYSTORE_PASSWORD = "keystorePassword";
        public static final String OPERATION_TIMEOUT = "operationTimeout";
        public static final String REQUEST_NEW_KERBEROS_TICKET = "requestNewKerberosTicket";
        public static final String WORKING_DIRECTORY = "workingDirectory";
        public static final String CONFIGURATION_NAME = "configurationName";
        public static final String COMMAND_TYPE = "commandType";
    }
}
