package io.cloudslang.content.vmware.commons.entities;

public class DeployTemplateFromLibraryInputs {
    private final String host;
    private final String username;
    private final String password;
    private final String closeSession;
    private final String vmSource;
    private final String vmFolder;
    private final String vmName;
    private final String datastore;
    private final String cluster;
    private final String vmResourcePool;
    private final String vmDatacenter;
    private final String description;
    private final String vmIdentifierType;

    public DeployTemplateFromLibraryInputs(String host, String username, String password, String closeSession, String vmSource, String vmFolder,
                                           String vmName, String datastore, String cluster, String vmResourcePool, String vmDatacenter,
                                           String description, String vmIdentifierType) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.closeSession = closeSession;
        this.vmSource = vmSource;
        this.vmFolder = vmFolder;
        this.vmName = vmName;
        this.datastore = datastore;
        this.cluster = cluster;
        this.vmResourcePool = vmResourcePool;
        this.vmDatacenter = vmDatacenter;
        this.description = description;
        this.vmIdentifierType = vmIdentifierType;
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

    public String getCloseSession() {
        return closeSession;
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

    public String getVmDatacenter() {
        return vmDatacenter;
    }

    public String getDescription() {
        return description;
    }

    public String getVmIdentifierType() {
        return vmIdentifierType;
    }

    public static class CloneVmInputsBuilder {
        private String host;
        private String username;
        private String password;
        private String closeSession;
        private String vmSource;
        private String vmFolder;
        private String vmName;
        private String datastore;
        private String cluster;
        private String vmResourcePool;
        private String vmDatacenter;
        private String description;
        private String vmIdentifierType;

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

        public CloneVmInputsBuilder closeSession(String closeSession) {
            this.closeSession = closeSession;
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

        public CloneVmInputsBuilder vmDatacenter(String vmDatacenter) {
            this.vmDatacenter = vmDatacenter;
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

        public DeployTemplateFromLibraryInputs build() {
            return new DeployTemplateFromLibraryInputs(host, username, password, closeSession, vmSource, vmFolder, vmName, datastore, cluster, vmResourcePool, vmDatacenter, description, vmIdentifierType);
        }
    }
}
