/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.xml.utils.InputUtils;


/**
 * Created by markowis on 03/03/2016.
 */
public class CommonInputs {
    private String xmlDocument;
    private String xmlDocumentSource;
    private String xPathQuery;
    private boolean secureProcessing;
    private String username;
    private String password;
    private String trustAllRoots;
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;
    private String x509Hostnameverifier;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;

    public CommonInputs(CommonInputsBuilder builder) {
        this.xmlDocument = builder.xmlDocument;
        this.xmlDocumentSource = builder.xmlDocumentSource;
        this.xPathQuery = builder.xPathQuery;
        this.secureProcessing = builder.secureProcessing;
        this.username = builder.username;
        this.password = builder.password;
        this.trustAllRoots = builder.trustAllRoots;
        this.keystore = builder.keystore;
        this.keystorePassword = builder.keystorePassword;
        this.trustKeystore = builder.trustKeystore;
        this.trustPassword = builder.trustPassword;
        this.x509Hostnameverifier = builder.x509Hostnameverifier;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
    }

    public String getXmlDocument() {
        return xmlDocument;
    }

    public String getXPathQuery() {
        return xPathQuery;
    }

    public boolean getSecureProcessing() {
        return secureProcessing;
    }

    public String getXmlDocumentSource() {
        return xmlDocumentSource;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public String getKeystore() {
        return keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public String getX509Hostnameverifier() {
        return x509Hostnameverifier;
    }

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

    public static class CommonInputsBuilder {
        private String xmlDocument;
        private String xmlDocumentSource;
        private String xPathQuery;
        private boolean secureProcessing;
        private String username;
        private String password;
        private String trustAllRoots;
        private String keystore;
        private String keystorePassword;
        private String trustKeystore;
        private String trustPassword;
        private String x509Hostnameverifier;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public CommonInputsBuilder withXmlDocument(String inputValue) {
            this.xmlDocument = inputValue;
            return this;
        }

        public CommonInputsBuilder withXpathQuery(String inputValue) {
            this.xPathQuery = inputValue;
            return this;
        }

        public CommonInputsBuilder withSecureProcessing(String inputValue) {
            this.secureProcessing = Boolean.parseBoolean(inputValue);
            return this;
        }

        public CommonInputsBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public CommonInputsBuilder withXmlDocumentSource(String xmlDocumentSource) {
            this.xmlDocumentSource = InputUtils.validateXmlDocumentSource(xmlDocumentSource);
            return this;
        }

        public CommonInputsBuilder withUsername(String username) {
            this.username = username;
            return this;
        }


        public CommonInputsBuilder withTrustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public CommonInputsBuilder withKeystore(String keystore) {
            this.keystore = keystore;
            return this;
        }

        public CommonInputsBuilder withKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }

        public CommonInputsBuilder withTrustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        public CommonInputsBuilder withTrustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        public CommonInputsBuilder withX509HostnameVerifier(String x509HostnameVerifier) {
            this.x509Hostnameverifier = x509HostnameVerifier;
            return this;
        }

        public CommonInputsBuilder withProxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public CommonInputsBuilder withProxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public CommonInputsBuilder withProxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public CommonInputsBuilder withProxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }
    }
}
