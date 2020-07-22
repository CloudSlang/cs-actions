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
package io.cloudslang.content.ldap.entities;

import io.cloudslang.content.ldap.constants.Constants;

import static io.cloudslang.content.ldap.utils.InputBuilderUtils.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class DisableComputerAccountInput implements LDAPInput{

    private String host;
    private String OU;
    private String computerCommonName;
    private String username;
    private String password;
    private boolean useSSL;
    private boolean trustAllRoots;
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;

    private DisableComputerAccountInput() {
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
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }


    public static class Builder {

        private String host;
        private String OU;
        private String computerCommonName;
        private String username;
        private String password;
        private String useSSL;
        private String trustAllRoots;
        private String keystore;
        private String keystorePassword;
        private String trustKeystore;
        private String trustPassword;

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


        public DisableComputerAccountInput build() throws Exception {
            DisableComputerAccountInput input = new DisableComputerAccountInput();

            input.host = buildHost(host, true);

            input.OU = buildOU(OU, true);

            input.computerCommonName = buildComputerCommonName(computerCommonName,true);

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
