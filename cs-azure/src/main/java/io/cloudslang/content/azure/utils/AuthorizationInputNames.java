/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.utils;

import io.cloudslang.content.constants.InputNames;

/**
 * Created by victor on 28.09.2016.
 */
public final class AuthorizationInputNames extends InputNames {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CLIENT_ID = "clientId";
    public static final String LOGIN_AUTHORITY = "loginAuthority";
    public static final String RESOURCE = "resource";
    public static final String IDENTIFIER = "identifier";
    public static final String PRIMARY_OR_SECONDARY_KEY = "primaryOrSecondaryKey";
    public static final String EXPIRY = "expiry";

    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";
}
