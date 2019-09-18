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

import static org.apache.commons.lang3.StringUtils.EMPTY;

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
    public static FTPInputsBuilder builder() {
        return new FTPInputsBuilder();
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

    public static class FTPInputsBuilder {
        private String hostname = EMPTY;
        private String port = EMPTY;
        private String localFile = EMPTY;
        private String remoteFile = EMPTY;
        private String user = EMPTY;
        private String password = EMPTY;
        private String type = EMPTY;
        private String passive = EMPTY;
        private String characterSet = EMPTY;

        FTPInputsBuilder() {
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder hostname(@NotNull final String hostname) {
            this.hostname = hostname;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder port(@NotNull final String port) {
            this.port = port;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder localFile(@NotNull final String localFile) {
            this.localFile = localFile;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder remoteFile(@NotNull final String remoteFile) {
            this.remoteFile = remoteFile;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder user(@NotNull final String user) {
            this.user = user;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder password(@NotNull final String password) {
            this.password = password;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder type(@NotNull final String type) {
            this.type = type;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder passive(@NotNull final String passive) {
            this.passive = passive;
            return this;
        }

        @NotNull
        public FTPInputs.FTPInputsBuilder characterSet(@NotNull final String characterSet) {
            this.characterSet = characterSet;
            return this;
        }

        public FTPInputs build() {
            return new FTPInputs(hostname, port, localFile, remoteFile, user, password, type, passive, characterSet);
        }


    }

}


