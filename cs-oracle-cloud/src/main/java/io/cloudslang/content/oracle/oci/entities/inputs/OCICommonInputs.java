

package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class OCICommonInputs {
    private final String compartmentOcid;
    private final String tenancyOcid;
    private final String userOcid;
    private final String fingerPrint;
    private final String privateKeyData;
    private final String privateKeyFile;
    private final String apiVersion;
    private final String region;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String connectTimeout;
    private final String socketTimeout;
    private final String keepAlive;
    private final String connectionsMaxPerRoot;
    private final String connectionsMaxTotal;
    private final String instanceId;
    private final String vnicId;
    private final String volumeId;
    private final String availabilityDomain;
    private final String page;
    private final String limit;

    @java.beans.ConstructorProperties({"compartmentOcid", "tenancyOcid", "userOcid", "fingerPrint", "privateKeyData", "privateKeyFile", "apiVersion", "region", "proxyHost", "proxyPort", "proxyUsername", "proxyPassword", "connectTimeout", "socketTimeout", "keepAlive", "connectionsMaxPerRoot", "connectionsMaxTotal", "instanceId", "vnicId", "volumeId", "availabilityDomain", "page", "limit"})
    private OCICommonInputs(String compartmentOcid, String tenancyOcid, String userOcid, String fingerPrint, String privateKeyData, String privateKeyFile, String apiVersion, String region, String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, String connectTimeout, String socketTimeout, String keepAlive, String connectionsMaxPerRoot, String connectionsMaxTotal, String instanceId, String vnicId, String volumeId, String availabilityDomain, String page, String limit) {
        this.compartmentOcid = compartmentOcid;
        this.tenancyOcid = tenancyOcid;
        this.userOcid = userOcid;
        this.fingerPrint = fingerPrint;
        this.privateKeyData = privateKeyData;
        this.privateKeyFile = privateKeyFile;
        this.apiVersion = apiVersion;
        this.region = region;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.keepAlive = keepAlive;
        this.connectionsMaxPerRoot = connectionsMaxPerRoot;
        this.connectionsMaxTotal = connectionsMaxTotal;
        this.instanceId = instanceId;
        this.vnicId = vnicId;
        this.volumeId = volumeId;
        this.availabilityDomain = availabilityDomain;
        this.page = page;
        this.limit = limit;
    }

    @NotNull
    public static OCICommonInputs.OCICommonInputsBuilder builder() {
        return new OCICommonInputs.OCICommonInputsBuilder();
    }

    @NotNull
    public String getCompartmentOcid() {
        return compartmentOcid;
    }

    @NotNull
    public String getTenancyOcid() {
        return tenancyOcid;
    }

    @NotNull
    public String getUserOcid() {
        return userOcid;
    }

    @NotNull
    public String getFingerPrint() {
        return fingerPrint;
    }

    @NotNull
    public String getPrivateKeyData() {
        return privateKeyData;
    }

    @NotNull
    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    @NotNull
    public String getAPIVersion() {
        return apiVersion;
    }

    @NotNull
    public String getRegion() {
        return region;
    }

    @NotNull
    public String getProxyHost() {
        return this.proxyHost;
    }

    @NotNull
    public String getProxyPort() {
        return this.proxyPort;
    }

    @NotNull
    public String getProxyUsername() {
        return this.proxyUsername;
    }

    @NotNull
    public String getProxyPassword() {
        return this.proxyPassword;
    }


    @NotNull
    public String getConnectTimeout() {
        return this.connectTimeout;
    }

    @NotNull
    public String getSocketTimeout() {
        return this.socketTimeout;
    }

    @NotNull
    public String getKeepAlive() {
        return this.keepAlive;
    }

    @NotNull
    public String getConnectionsMaxPerRoot() {
        return connectionsMaxPerRoot;
    }

    @NotNull
    public String getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }


    @NotNull
    public String getInstanceId() {
        return this.instanceId;
    }

    @NotNull
    public String getVnicId() {
        return this.vnicId;
    }

    @NotNull
    public String getVolumeId() {
        return this.volumeId;
    }

    @NotNull
    public String getPage() {
        return this.page;
    }

    @NotNull
    public String getLimit() {
        return this.limit;
    }

    @NotNull
    public String getAvailabilityDomain() {
        return this.availabilityDomain;
    }

    public static class OCICommonInputsBuilder {
        private String compartmentOcid = EMPTY;
        private String tenancyOcid = EMPTY;
        private String userOcid = EMPTY;
        private String fingerPrint = EMPTY;
        private String privateKeyData = EMPTY;
        private String privateKeyFile = EMPTY;
        private String apiVersion = EMPTY;
        private String region = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String connectTimeout = EMPTY;
        private String socketTimeout = EMPTY;
        private String keepAlive = EMPTY;
        private String connectionsMaxPerRoot = EMPTY;
        private String connectionsMaxTotal = EMPTY;
        private String instanceId = EMPTY;
        private String vnicId = EMPTY;
        private String volumeId = EMPTY;
        private String availabilityDomain = EMPTY;
        private String page = EMPTY;
        private String limit = EMPTY;

        OCICommonInputsBuilder() {
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder compartmentOcid(@NotNull final String compartmentOcid) {
            this.compartmentOcid = compartmentOcid;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder tenancyOcid(@NotNull final String tenancyOcid) {
            this.tenancyOcid = tenancyOcid;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder userOcid(@NotNull final String userOcid) {
            this.userOcid = userOcid;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder fingerPrint(@NotNull final String fingerPrint) {
            this.fingerPrint = fingerPrint;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder privateKeyData(@NotNull final String privateKeyData) {
            this.privateKeyData = privateKeyData;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder privateKeyFile(@NotNull final String privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder apiVersion(@NotNull final String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder region(@NotNull final String region) {
            this.region = region;
            return this;
        }


        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder connectTimeout(@NotNull final String connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder socketTimeout(@NotNull final String socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder keepAlive(@NotNull final String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }


        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder connectionsMaxPerRoot(@NotNull final String connectionsMaxPerRoot) {
            this.connectionsMaxPerRoot = connectionsMaxPerRoot;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder connectionsMaxTotal(@NotNull final String connectionsMaxTotal) {
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }


        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder instanceId(@NotNull final String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder vnicId(@NotNull final String vnicId) {
            this.vnicId = vnicId;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder volumeId(@NotNull final String volumeId) {
            this.volumeId = volumeId;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder availabilityDomain(@NotNull final String availabilityDomain) {
            this.availabilityDomain = availabilityDomain;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder page(@NotNull final String page) {
            this.page = page;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder limit(@NotNull final String limit) {
            this.limit = limit;
            return this;
        }

        public OCICommonInputs build() {
            return new OCICommonInputs(compartmentOcid, tenancyOcid, userOcid, fingerPrint, privateKeyData, privateKeyFile, apiVersion, region, proxyHost, proxyPort, proxyUsername, proxyPassword, connectTimeout,
                    socketTimeout, keepAlive, connectionsMaxPerRoot, connectionsMaxTotal, instanceId, vnicId, volumeId, availabilityDomain, page, limit);
        }
    }
}
