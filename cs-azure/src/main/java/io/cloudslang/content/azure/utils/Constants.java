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

/**
 * Created by victor on 07.10.2016.
 */
public final class Constants {
    public static final String FORWARD_SLASH = "/";
    public static final String COMMA = ",";
    public static final String NEW_LINE = "\n";
    public static final String DEFAULT_CLIENT_ID = "9ba1a5c7-f17a-4de9-a1f1-6178c8d51223";
    public static final String DEFAULT_AUTHORITY = "https://login.windows.net/common";
    public static final String DEFAULT_RESOURCE = "https://management.azure.com";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String DEFAULT_TIMEOUT = "0";

    public static final String SHARED_ACCESS_SIGNATURE = "SharedAccessSignature uid=%s&ex=%s&sn=%s";

    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid port";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s is not a valid number";

    public static final String PROXY_HTTP_USER = "http.proxyUser";
    public static final String PROXY_HTTP_PASSWORD = "http.proxyPassword";

    public static final String STORAGE_AUTH_ENDPOINT = "DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s";
}