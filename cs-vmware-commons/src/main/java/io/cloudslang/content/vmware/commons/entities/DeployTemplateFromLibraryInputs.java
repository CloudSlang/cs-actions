/*
 * (c) Copyright 2023 Open Text
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
package io.cloudslang.content.vmware.commons.entities;

public class DeployTemplateFromLibraryInputs {
    private final String host;
    private final String username;
    private final String password;
    private String vmSource;
    private String vmFolder;
    private final String vmName;
    private String datastore;
    private String cluster;
    private String vmResourcePool;
    private final String description;
    private final String vmIdentifierType;
    private String hostSystem;
    private final String timeout;

    public DeployTemplateFromLibraryInputs(String host, String username, String password, String vmSource, String vmFolder,
                                           String vmName, String datastore, String cluster, String vmResourcePool,
                                           String description, String vmIdentifierType, String hostSystem, String timeout) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.vmSource = vmSource;
        this.vmFolder = vmFolder;
        this.vmName = vmName;
        this.datastore = datastore;
        this.cluster = cluster;
        this.vmResourcePool = vmResourcePool;
        this.description = description;
        this.vmIdentifierType = vmIdentifierType;
        this.hostSystem = hostSystem;
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVmSource() {
        return vmSource;
    }

    public String getVmFolder() {
        return vmFolder;
    }

    public String getVmName() {
        return vmName;
    }

    public String getDatastore() {
        return datastore;
    }

    public String getCluster() {
        return cluster;
    }

    public String getVmResourcePool() {
        return vmResourcePool;
    }

    public String getTimeout() {
        return timeout;
    }

    public String getDescription() {
        return description;
    }

    public String getHostSystem() {
        return hostSystem;
    }

    public String getVmIdentifierType() {
        return vmIdentifierType;
    }

    public void setVmSource(String vmSource) {
        this.vmSource = vmSource;
    }

    public void setVmFolder(String vmFolder) {
        this.vmFolder = vmFolder;
    }

    public void setDatastore(String datastore) {
        this.datastore = datastore;
    }

    public void setVmResourcePool(String vmResourcePool) {
        this.vmResourcePool = vmResourcePool;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setHostSystem(String hostSystem) {
        this.hostSystem = hostSystem;
    }

    public static class CloneVmInputsBuilder {
        private String host;
        private String username;
        private String password;
        private String vmSource;
        private String vmFolder;
        private String vmName;
        private String datastore;
        private String cluster;
        private String vmResourcePool;
        private String description;
        private String vmIdentifierType;
        private String hostSystem;
        private String timeout;

        public CloneVmInputsBuilder host(String host) {
            this.host = host;
            return this;
        }

        public CloneVmInputsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CloneVmInputsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CloneVmInputsBuilder vmSource(String vmSource) {
            this.vmSource = vmSource;
            return this;
        }

        public CloneVmInputsBuilder vmFolder(String vmFolder) {
            this.vmFolder = vmFolder;
            return this;
        }

        public CloneVmInputsBuilder vmName(String vmName) {
            this.vmName = vmName;
            return this;
        }

        public CloneVmInputsBuilder datastore(String datastore) {
            this.datastore = datastore;
            return this;
        }

        public CloneVmInputsBuilder cluster(String cluster) {
            this.cluster = cluster;
            return this;
        }

        public CloneVmInputsBuilder vmResourcePool(String vmResourcePool) {
            this.vmResourcePool = vmResourcePool;
            return this;
        }

        public CloneVmInputsBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CloneVmInputsBuilder vmIdentifierType(String vmIdentifierType) {
            this.vmIdentifierType = vmIdentifierType;
            return this;
        }

        public CloneVmInputsBuilder hostSystem(String hostSystem) {
            this.hostSystem = hostSystem;
            return this;
        }

        public CloneVmInputsBuilder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        public DeployTemplateFromLibraryInputs build() {
            return new DeployTemplateFromLibraryInputs(host, username, password, vmSource, vmFolder, vmName, datastore,
                    cluster, vmResourcePool, description, vmIdentifierType, hostSystem, timeout);
        }
    }
}
