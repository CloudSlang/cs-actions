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

public class CreateComputerAccountInput implements CreateCompAccountInput {

    private String host;
    private String distinguishedName;
    private String computerCommonName;
    private String sAMAccountName;
    private String username;
    private String password;
    private String protocol;
    private boolean trustAllRoots;
    private String trustKeystore;
    private String trustPassword;
    private boolean escapeChars;
    private String connectionTimeout;
    private String executionTimeout;

    private CreateComputerAccountInput() {
    }

    public String getHost() {
        return host;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public String getComputerCommonName() {
        return computerCommonName;
    }

    public String getSAMAccountName() {
        return sAMAccountName;
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

    public Boolean getEscapeChars() {
        return escapeChars;
    }

    public String getConnectionTimeout() { return connectionTimeout; }

    public String getExecutionTimeout() {
        return executionTimeout;
    }

    public static class Builder {

        private String host;
        private String distinguishedName;
        private String computerCommonName;
        private String sAMAccountName;
        private String username;
        private String password;
        private String protocol;
        private String trustAllRoots;
        private String trustKeystore;
        private String trustPassword;
        private String escapeChars;
        private String connectionTimeout;
        private String executionTimeout;


        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder distinguishedName(String distinguishedName) {
            this.distinguishedName = distinguishedName;
            return this;
        }

        public Builder computerCommonName(String computerCommonName) {
            this.computerCommonName = computerCommonName;
            return this;
        }

        public Builder sAMAccountName(String sAMAccountName) {
            this.sAMAccountName = sAMAccountName;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }


        public Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }


        public Builder escapeChars(String escapeChars) {
            this.escapeChars = escapeChars;
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

        public CreateComputerAccountInput build() throws Exception {
            CreateComputerAccountInput input = new CreateComputerAccountInput();

            input.host = buildHost(host, true);

            input.distinguishedName = buildDistinguishedName(distinguishedName, true);

            input.computerCommonName = buildComputerCommonName(computerCommonName, true);

            input.sAMAccountName = buildSAMAccountName(sAMAccountName, computerCommonName);

            input.username = buildUsername(username);

            input.password = buildPassword(password);

            input.trustAllRoots = buildTrustAllRoots(trustAllRoots);

            input.protocol = buildProtocol(protocol);

            input.trustKeystore = defaultIfEmpty(trustKeystore, Constants.DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            input.escapeChars = buildEscapeChars(escapeChars);

            input.connectionTimeout = connectionTimeout;

            input.executionTimeout = executionTimeout;

            return input;
        }
    }
}
