package io.cloudslang.content.azure.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AuthorizationTokenUsingWebAPIInputs {
    private final String tenantId;
    private final String clientSecret;
    private final String clientId;
    private final String subscriptionId;
    private final String resource;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;

    @java.beans.ConstructorProperties({"tenantId", "clientSecret", "clientId", "subscriptionId", "resource", "proxyHost", "proxyPort", "proxyUsername", "proxyPassword"})
    private AuthorizationTokenUsingWebAPIInputs(final String tenantId, final String clientSecret, final String clientId, final String subscriptionId,
                                                final String resource, final String proxyHost, final String proxyPort, final String proxyUsername, final String proxyPassword,
                                                String trustAllRoots, String x509HostnameVerifier, String trustKeystore, String trustPassword) {
        this.tenantId = tenantId;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
        this.subscriptionId = subscriptionId;
        this.resource = resource;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.trustAllRoots = trustAllRoots;
        this.x509HostnameVerifier = x509HostnameVerifier;
        this.trustKeystore = trustKeystore;
        this.trustPassword = trustPassword;
    }

    @NotNull
    public static AuthorizationTokenUsingWebAPIInputsBuilder builder() {
        return new AuthorizationTokenUsingWebAPIInputsBuilder();
    }

    @NotNull
    public String getTenantId() {
        return this.tenantId;
    }

    @NotNull
    public String getClientSecret() {
        return this.clientSecret;
    }

    @NotNull
    public String getClientId() {
        return this.clientId;
    }

    @NotNull
    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    @NotNull
    public String getResource() {
        return this.resource;
    }

    @NotNull
    public String getProxyHost() {
        return this.proxyHost;
    }

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
    public String getTrustAllRoots() {
        return this.trustAllRoots;
    }

    @NotNull
    public String getX509HostnameVerifier() {
        return this.x509HostnameVerifier;
    }

    @NotNull
    public String getTrustKeystore() {
        return this.trustKeystore;
    }

    @NotNull
    public String getTrustPassword() {
        return this.trustPassword;
    }


    public static class AuthorizationTokenUsingWebAPIInputsBuilder {
        private String tenantId = EMPTY;
        private String clientSecret = EMPTY;
        private String clientId = EMPTY;
        private String subscriptionId = EMPTY;
        private String resource = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String trustAllRoots = EMPTY;
        private String x509HostnameVerifier = EMPTY;
        private String trustKeystore = EMPTY;
        private String trustPassword = EMPTY;

        AuthorizationTokenUsingWebAPIInputsBuilder() {
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder tenantId(@NotNull final String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder clientSecret(@NotNull final String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder clientId(@NotNull final String clientId) {
            this.clientId = clientId;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder subscriptionId(@NotNull final String subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder resource(@NotNull final String resource) {
            this.resource = resource;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder trustAllRoots(@NotNull final String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder x509HostnameVerifier(@NotNull final String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder trustKeystore(@NotNull final String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder trustPassword(@NotNull final String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputs build() {
            return new AuthorizationTokenUsingWebAPIInputs(tenantId, clientSecret, clientId, subscriptionId, resource, proxyHost, proxyPort, proxyUsername, proxyPassword,
                    trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword);
        }

        @Override
        public String toString() {
            return String.format("AuthorizationTokenUsingWebAPIInputsBuilder(tenantId=%s, clientSecret=%s, clientId=%s, subscriptionId=%s," +
                            " resource=%s, proxyHost=%s, proxyPort=%s, proxyUsername=%s, proxyPassword=%s,  trustAllRoots=%s, x509HostnameVerifier=%s, trustKeystore=%s, trustPassword=%s)",
                    this.tenantId, this.clientSecret, this.clientId, this.subscriptionId, this.resource, this.proxyHost,
                    this.proxyPort, this.proxyUsername, this.proxyPassword, this.trustAllRoots, this.x509HostnameVerifier, this.trustKeystore, this.trustPassword);
        }
    }
}
