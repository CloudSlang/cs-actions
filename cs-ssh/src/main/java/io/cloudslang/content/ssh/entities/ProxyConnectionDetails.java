/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.ssh.entities;


import org.apache.commons.lang3.builder.EqualsBuilder;

public class ProxyConnectionDetails {

    private String proxyHost;
    private String proxyUsername;
    private String proxyPassword;
    private int proxyPort;

    public ProxyConnectionDetails() {
    }

    public ProxyConnectionDetails(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
        this(proxyHost, proxyPort);
        this.setProxyUsername(proxyUsername);
        this.setProxyPassword(proxyPassword);
    }

    public ProxyConnectionDetails(String proxyHost, int proxyPort) {
        this.setProxyHost(proxyHost);
        this.setProxyPort(proxyPort);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ProxyConnectionDetails that = (ProxyConnectionDetails) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(proxyPort, that.getProxyPort())
                .append(proxyHost, that.getProxyHost())
                .append(proxyUsername, that.getProxyUsername())
                .append(proxyPassword, that.getProxyPassword())
                .isEquals();
    }

    @Override
    public int hashCode() {
        int result = proxyHost != null ? proxyHost.hashCode() : 0;
        result = 31 * result + proxyPort;
        result = 31 * result + (proxyUsername != null ? proxyUsername.hashCode() : 0);
        result = 31 * result + (proxyPassword != null ? proxyPassword.hashCode() : 0);
        return result;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
}
