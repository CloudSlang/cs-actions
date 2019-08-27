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
package io.cloudslang.content.rft.entities;

import org.jetbrains.annotations.NotNull;

public class FTPInputs {

    private final String hostname;
    private final String port;
    private final String localFile;
    private final String remoteFile;
    private final String user;
    private final String password;
    private final String type;
    private final String passive;
    private final String characterSet;

    @java.beans.ConstructorProperties({"hostname", "port", "localFile", "remoteFile", "user", "password", "type", "passive", "characterSet"})

    public FTPInputs(String hostname, String port, String localFile, String remoteFile, String user, String password, String type, String passive, String characterSet) {
        this.hostname = hostname;
        this.port = port;
        this.localFile = localFile;
        this.remoteFile = remoteFile;
        this.user = user;
        this.password = password;
        this.type = type;
        this.passive = passive;
        this.characterSet = characterSet;
    }

    @NotNull
    public String getHostname() {
        return hostname;
    }

    @NotNull
    public String getPort() {
        return port;
    }

    @NotNull
    public String getLocalFile() {
        return localFile;
    }

    @NotNull
    public String getRemoteFile() {
        return remoteFile;
    }

    @NotNull
    public String getUser() {
        return user;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public String getPassive() {
        return passive;
    }

    @NotNull
    public String getCharacterSet() {
        return characterSet;
    }
}


