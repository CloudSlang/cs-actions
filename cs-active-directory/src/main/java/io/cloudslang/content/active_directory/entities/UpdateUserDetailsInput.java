/*
 * Copyright 2021-2024 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.active_directory.entities;

import io.cloudslang.content.active_directory.constants.Constants;

import java.util.List;

import static io.cloudslang.content.active_directory.utils.InputBuilderUtils.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class UpdateUserDetailsInput implements UpdateUserDetailsInterface {

    private String host;
    private String distinguishedName;
    private String userCommonName;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String displayName;
    private String city;
    private String street;
    private String stateOrProvince;
    private String zipOrPostalCode;
    private String countryOrRegion;
    private String attributesList;
    private String protocol;
    private String proxyHost;
    private int proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String tlsVersion;
    private List<String> allowedCiphers;
    private String x509HostnameVerifier;
    private boolean trustAllRoots;
    private String trustKeystore;
    private String trustPassword;
    private String timeout;

    private UpdateUserDetailsInput() {
    }

    public String getHost() {
        return host;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public String getZipOrPostalCode() {
        return zipOrPostalCode;
    }

    public String getCountryOrRegion() {
        return countryOrRegion;
    }

    public String getAttributesList() {
        return attributesList;
    }

    public String getUserCommonName() {
        return userCommonName;
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

    public String getTimeout() {
        return timeout;
    }

    public String getTlsVersion() {
        return tlsVersion;
    }

    public List<String> getAllowedCiphers() {
        return allowedCiphers;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    public static class Builder {

        private String host;
        private String distinguishedName;
        private String userCommonName;
        private String username;
        private String password;
        private String protocol;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String tlsVersion;
        private String allowedCiphers;
        private String x509HostnameVerifier;
        private String trustAllRoots;
        private String trustKeystore;
        private String trustPassword;
        private String firstName;
        private String lastName;
        private String displayName;
        private String city;
        private String street;
        private String stateOrProvince;
        private String zipOrPostalCode;
        private String countryOrRegion;
        private String attributesList;
        private String timeout;


        public UpdateUserDetailsInput.Builder host(String host) {
            this.host = host;
            return this;
        }

        public UpdateUserDetailsInput.Builder distinguishedName(String distinguishedName) {
            this.distinguishedName = distinguishedName;
            return this;
        }

        public UpdateUserDetailsInput.Builder userCommonName(String userCommonName) {
            this.userCommonName = userCommonName;
            return this;
        }


        public UpdateUserDetailsInput.Builder username(String username) {
            this.username = username;
            return this;
        }


        public UpdateUserDetailsInput.Builder password(String password) {
            this.password = password;
            return this;
        }

        public UpdateUserDetailsInput.Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public UpdateUserDetailsInput.Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public UpdateUserDetailsInput.Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public UpdateUserDetailsInput.Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }


        public UpdateUserDetailsInput.Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UpdateUserDetailsInput.Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UpdateUserDetailsInput.Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public UpdateUserDetailsInput.Builder city(String city) {
            this.city = city;
            return this;
        }

        public UpdateUserDetailsInput.Builder street(String street) {
            this.street = street;
            return this;
        }

        public UpdateUserDetailsInput.Builder stateOrProvince(String stateOrProvince) {
            this.stateOrProvince = stateOrProvince;
            return this;
        }

        public UpdateUserDetailsInput.Builder zipOrPostalCode(String zipOrPostalCode) {
            this.zipOrPostalCode = zipOrPostalCode;
            return this;
        }

        public UpdateUserDetailsInput.Builder countryOrRegion(String countryOrRegion) {
            this.countryOrRegion = countryOrRegion;
            return this;
        }

        public UpdateUserDetailsInput.Builder attributesList(String attributesList) {
            this.attributesList = attributesList;
            return this;
        }

        public UpdateUserDetailsInput.Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder tlsVersion(String tlsVersion) {
            this.tlsVersion = tlsVersion;
            return this;
        }

        public Builder allowedCiphers(String allowedCiphers) {
            this.allowedCiphers = allowedCiphers;
            return this;
        }

        public Builder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public Builder proxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public Builder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public Builder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public Builder x509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        public UpdateUserDetailsInput build() throws Exception {
            UpdateUserDetailsInput input = new UpdateUserDetailsInput();

            input.host = buildHost(host, true);

            input.distinguishedName = buildDistinguishedName(distinguishedName, true);

            input.userCommonName = buildUserCommonName(userCommonName, true);

            input.username = buildUsername(username, true);

            input.password = buildPassword(password, true);

            input.trustAllRoots = buildTrustAllRoots(trustAllRoots);

            input.protocol = buildProtocol(protocol, true);

            input.trustKeystore = defaultIfEmpty(trustKeystore, Constants.DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            input.firstName = firstName;

            input.lastName = lastName;

            input.displayName = displayName;

            input.city = city;

            input.street = street;

            input.stateOrProvince = stateOrProvince;

            input.zipOrPostalCode = zipOrPostalCode;

            input.countryOrRegion = countryOrRegion;

            input.attributesList = attributesList;

            input.timeout = timeout;

            input.proxyHost = proxyHost;

            input.proxyUsername = proxyUsername;

            input.proxyPassword = proxyPassword;

            input.x509HostnameVerifier = x509HostnameVerifier;

            input.tlsVersion = buildTlsVersions(tlsVersion);

            input.allowedCiphers = buildAllowedCiphers(allowedCiphers);

            input.proxyPort = Integer.parseInt(addVerifyPort(proxyPort));

            return input;
        }
    }
}
