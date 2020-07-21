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
package main.java.io.cloudslang.content.ldap.entities;

import main.java.io.cloudslang.content.ldap.constants.Constants;

import static main.java.io.cloudslang.content.ldap.utils.InputBuilderUtils.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateComputerAccountInput implements LDAPInput{

    private String host;
    private String OU;
    private String computerCommonName;
    private String sAMAccountName;
    private String username;
    private String password;
    private boolean useSSL;
    private boolean trustAllRoots;
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;
    private boolean escapeChars;

    private CreateComputerAccountInput() {
    }

    public String getHost() {
        return host;
    }

    public String getOU() {
        return OU;
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

    public Boolean getUseSSL() {
        return useSSL;
    }

    public Boolean getTrustAllRoots() {
        return trustAllRoots;
    }

    public String getKeyStore() {
        return keystore;
    }

    public String getKeyStorePassword() {
        return keystorePassword;
    }

    public String getTrustKeystore() {
        return null;
    }

    public String getTrustPassword() {
        return null;
    }

    public Boolean getEscapeChars() {
        return escapeChars;
    }

    public static class Builder {

        private String host;
        private String OU;
        private String computerCommonName;
        private String sAMAccountName;
        private String username;
        private String password;
        private String useSSL;
        private String trustAllRoots;
        private String keystore;
        private String keystorePassword;
        private String trustKeystore;
        private String trustPassword;
        private String escapeChars;


        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder OU(String OU) {
            this.OU = OU;
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

        public Builder useSSL(String useSSL) {
            this.useSSL = useSSL;
            return this;
        }

        public Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public Builder keyStore(String keystore) {
            this.keystore = keystore;
            return this;
        }


        public Builder keyStorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
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

        public CreateComputerAccountInput build() throws Exception {
            CreateComputerAccountInput input = new CreateComputerAccountInput();

            input.host = buildHost(host);

            input.OU = buildOU(OU);

            input.computerCommonName = buildComputerCommonName(computerCommonName,true);

            input.sAMAccountName = buildSAMAccountName(sAMAccountName,computerCommonName);

            input.username = buildUsername(username);

            input.password = buildPassword(password);

            input.trustAllRoots = buildTrustAllRoots(trustAllRoots);

            input.useSSL = buildUseSSL(useSSL);

            input.keystore = buildKeystore(keystore);

            input.keystorePassword = keystorePassword;

            input.trustKeystore = defaultIfEmpty(trustKeystore, Constants.DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            return input;
        }
    }
}
