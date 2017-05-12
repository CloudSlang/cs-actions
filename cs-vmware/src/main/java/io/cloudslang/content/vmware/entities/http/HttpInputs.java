/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities.http;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.utils.BooleanUtilities;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class HttpInputs {
    private static final int DEFAULT_HTTPS_PORT = 443;

    private final String host;
    private final int port;
    private final String protocol;
    private final String username;
    private final String password;
    private final boolean trustEveryone;
    private final boolean closeSession;
    private final GlobalSessionObject<Map<String, Connection>> globalSessionObject;

    public HttpInputs(HttpInputsBuilder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.protocol = builder.protocol;
        this.username = builder.username;
        this.password = builder.password;
        this.trustEveryone = builder.trustEveryone;
        this.closeSession = builder.closeSession;
        this.globalSessionObject = builder.globalSessionObject;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isTrustEveryone() {
        return trustEveryone;
    }

    public boolean isCloseSession() {
        return closeSession;
    }

    public GlobalSessionObject<Map<String, Connection>> getGlobalSessionObject() {
        return globalSessionObject;
    }

    public static class HttpInputsBuilder {
        private String host;
        private int port;
        private String protocol;
        private String username;
        private String password;
        private boolean trustEveryone;
        private boolean closeSession;
        private GlobalSessionObject<Map<String, Connection>> globalSessionObject;

        public HttpInputs build() {
            return new HttpInputs(this);
        }

        public HttpInputsBuilder withHost(String inputValue) {
            host = inputValue;
            return this;
        }

        public HttpInputsBuilder withPort(String inputValue) {
            port = InputUtils.getIntInput(inputValue, DEFAULT_HTTPS_PORT);
            return this;
        }

        public HttpInputsBuilder withProtocol(String inputValue) throws Exception {
            protocol = Protocol.getValue(inputValue);
            return this;
        }

        public HttpInputsBuilder withUsername(String inputValue) throws Exception {
            username = inputValue;
            return this;
        }

        public HttpInputsBuilder withPassword(String inputValue) throws Exception {
            password = inputValue;
            return this;
        }

        public HttpInputsBuilder withTrustEveryone(String inputValue) throws Exception {
            trustEveryone = BooleanUtilities.toBoolean(inputValue);
            return this;
        }

        public HttpInputsBuilder withCloseSession(String inputValue) throws Exception {
            closeSession = BooleanUtilities.toBoolean(inputValue);
            return this;
        }

        public HttpInputsBuilder withGlobalSessionObject(GlobalSessionObject<Map<String, Connection>> globalSessionObject) throws Exception {
            this.globalSessionObject = globalSessionObject;
            return this;
        }
    }
}
