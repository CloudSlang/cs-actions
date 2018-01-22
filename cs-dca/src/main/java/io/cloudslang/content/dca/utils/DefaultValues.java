/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dca.utils;

public class DefaultValues {
    public static final String DEFAULT_IDM_PORT = "5443";
    public static final String DEFAULT_IDM_PROTOCOL = Constants.HTTPS;
    public static final String DEFAULT_TENANT = "DEFAULT_TENANT";

    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String DEFAULT_JAVA_KEYSTORE_PASSWORD = "changeit";
    public static final String DEFAULT_DCA_PORT = "443";
    public static final String DEFAULT_TIMEOUT = "1200";
    public static final String DEFAULT_POLLING_INTERVAL = "30";
}
