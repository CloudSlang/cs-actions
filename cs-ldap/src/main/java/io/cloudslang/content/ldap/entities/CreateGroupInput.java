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

public class CreateGroupInput implements CreateGroupInputInterface {

    private String host;
    private String distinguishedName;
    private String groupCommonName;
    private String groupType;
    private String sAMAccountName;
    private String username;
    private String password;
    private String protocol;
    private boolean trustAllRoots;
    private String trustKeystore;
    private String trustPassword;
    private boolean escapeChars;
    private int connectionTimeout;
    private int executionTimeout;

    public CreateGroupInput() {
    }

    public String getHost() {
        return host;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public String getGroupCommonName() {
        return groupCommonName;
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

    public String getGroupType() { return groupType; }

    public Integer getConnectionTimeout() { return connectionTimeout; }

    public Integer getExecutionTimeout() {
        return executionTimeout;
    }

    public static class Builder {

        private String host;
        private String distinguishedName;
        private String groupCommonName;
        private String groupType;
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


        public CreateGroupInput.Builder host(String host) {
            this.host = host;
            return this;
        }

        public CreateGroupInput.Builder distinguishedName(String distinguishedName) {
            this.distinguishedName = distinguishedName;
            return this;
        }

        public CreateGroupInput.Builder groupCommonName(String groupCommonName) {
            this.groupCommonName = groupCommonName;
            return this;
        }

        public CreateGroupInput.Builder groupType(String groupType) {
            this.groupType = groupType;
            return this;
        }

        public CreateGroupInput.Builder sAMAccountName(String sAMAccountName) {
            this.sAMAccountName = sAMAccountName;
            return this;
        }

        public CreateGroupInput.Builder username(String username) {
            this.username = username;
            return this;
        }


        public CreateGroupInput.Builder password(String password) {
            this.password = password;
            return this;
        }

        public CreateGroupInput.Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public CreateGroupInput.Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public CreateGroupInput.Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public CreateGroupInput.Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }


        public CreateGroupInput.Builder escapeChars(String escapeChars) {
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


        public CreateGroupInput build() throws Exception {
            CreateGroupInput input = new CreateGroupInput();

            input.host = buildHost(host, true);

            input.distinguishedName = buildDistinguishedName(distinguishedName, true);

            input.groupCommonName = buildGroupCommonName(groupCommonName, true);

            input.groupType = buildUserPassword(groupType);

            input.sAMAccountName = buildSAMAccountNameRequired(sAMAccountName, true);

            input.username = buildUsername(username);

            input.password = buildPassword(password);

            input.trustAllRoots = buildTrustAllRoots(trustAllRoots);

            input.protocol = buildProtocol(protocol);

            input.trustKeystore = defaultIfEmpty(trustKeystore, Constants.DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            input.escapeChars = buildEscapeChars(escapeChars);

            input.connectionTimeout = buildConnectionTimeout(connectionTimeout);

            input.executionTimeout = buildExecutionTimeout(executionTimeout);

            return input;
        }
    }
}
