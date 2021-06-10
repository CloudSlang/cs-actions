/*
 * (c) Copyright 2020 Micro Focus
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
package io.cloudslang.content.ldap.entities;

import io.cloudslang.content.ldap.constants.Constants;

import static io.cloudslang.content.ldap.utils.InputBuilderUtils.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class AuthenticateUserInput implements AuthenticateUserInterface {

    private String host;
    private String rootDistinguishedName;
    private String username;
    private String password;
    private String protocol;
    private boolean trustAllRoots;
    private String trustKeystore;
    private String trustPassword;
    private int connectionTimeout;
    private int executionTimeout;

    private AuthenticateUserInput() {
    }

    public String getHost() {
        return host;
    }

    public String getRootDistinguishedName() {
        return rootDistinguishedName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProtocol() {
        return protocol;
    }

    public Boolean getTrustAllRoots() {
        return trustAllRoots;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public Integer getConnectionTimeout() { return connectionTimeout; }

    public Integer getExecutionTimeout() {
        return executionTimeout;
    }


    public static class Builder {

        private String host;
        private String rootDistinguishedName;
        private String username;
        private String password;
        private String protocol;
        private String trustAllRoots;
        private String trustKeystore;
        private String trustPassword;
        private String connectionTimeout;
        private String executionTimeout;


        public AuthenticateUserInput.Builder host(String host) {
            this.host = host;
            return this;
        }

        public AuthenticateUserInput.Builder rootDistinguishedName(String rootDistinguishedName) {
            this.rootDistinguishedName = rootDistinguishedName;
            return this;
        }

        public AuthenticateUserInput.Builder username(String username) {
            this.username = username;
            return this;
        }


        public AuthenticateUserInput.Builder password(String password) {
            this.password = password;
            return this;
        }

        public AuthenticateUserInput.Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public AuthenticateUserInput.Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public AuthenticateUserInput.Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public AuthenticateUserInput.Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        public Builder connectionTimeout(String connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder executionTimeout(String executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }


        public AuthenticateUserInput build() throws Exception {
            AuthenticateUserInput input = new AuthenticateUserInput();

            input.host = buildHost(host, true);

            input.rootDistinguishedName = buildRootDN(rootDistinguishedName, true);

            input.username = buildUsername(username);

            input.password = buildPassword(password);

            input.trustAllRoots = buildTrustAllRoots(trustAllRoots);

            input.protocol = buildProtocol(protocol);

            input.trustKeystore = defaultIfEmpty(trustKeystore, Constants.DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            input.connectionTimeout = buildConnectionTimeout(connectionTimeout);

            input.executionTimeout = buildExecutionTimeout(executionTimeout);

            return input;
        }
    }
}
