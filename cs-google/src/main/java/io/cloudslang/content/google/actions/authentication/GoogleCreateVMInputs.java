package io.cloudslang.content.google.actions.authentication;

import org.jetbrains.annotations.NotNull;

public class GoogleCreateVMInputs {
    public String getProjectId() {
        return projectId;
    }

    public String getZone() {
        return zone;
    }

    public String getFilter() {
        return filter;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getAccessToken() {
        return accessToken;
    }

    private final String projectId;
    private final String zone;
    private final String filter;
    private final String orderBy;
    private final String accessToken;

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;

    @java.beans.ConstructorProperties({"projectId","zone","filter","orderBy","accessToken","proxyHost","proxyPort","proxyUsername","proxyPassword"})
    public GoogleCreateVMInputs(String projectId, String zone, String filter, String orderBy, String accessToken, String proxyHost,String proxyPort, String proxyUsername,String proxyPassword) {
        this.projectId = projectId;
        this.zone = zone;
        this.filter = filter;
        this.orderBy = orderBy;
        this.accessToken = accessToken;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    @NotNull
    public static GoogleCreateVMInputs.GoogleCreateVMInputsBuilder builder() {
        return new GoogleCreateVMInputs.GoogleCreateVMInputsBuilder();
    }

    public static final class GoogleCreateVMInputsBuilder {
        private  String projectId;
        private  String zone;
        private  String filter;
        private  String orderBy;
        private  String accessToken;
        private  String proxyHost;
        private  String proxyPort;
        private  String proxyUsername;
        private  String proxyPassword;

        private GoogleCreateVMInputsBuilder() {
        }

        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }
        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder proxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }
        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }
        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder zone(String zone) {
            this.zone = zone;
            return this;
        }
        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder filter(String filter) {
            this.filter = filter;
            return this;
        }
        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder orderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }
        @NotNull
        public GoogleCreateVMInputs.GoogleCreateVMInputsBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        @NotNull
        public GoogleCreateVMInputs build() {
            return new GoogleCreateVMInputs(projectId,zone,filter,orderBy, accessToken,proxyHost,proxyPort,proxyUsername,proxyPassword);
        }
    }
}
