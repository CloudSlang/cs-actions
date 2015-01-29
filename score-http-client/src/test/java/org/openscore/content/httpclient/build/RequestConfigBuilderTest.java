/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.content.httpclient.build;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/29/14
 */

import org.apache.http.HttpException;
import org.apache.http.client.config.RequestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URISyntaxException;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/28/14
 */
public class RequestConfigBuilderTest {

    private RequestConfigBuilder requestConfigBuilder;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        requestConfigBuilder = new RequestConfigBuilder();
    }

    @After
    public void tearDown() {
        requestConfigBuilder = null;
    }

    @Test
    public void buildProxyRoute() throws URISyntaxException, HttpException {
        RequestConfig reqConfig = requestConfigBuilder
                .setProxyHost("myproxy.com")
                .setProxyPort("80")
                .setSocketTimeout("-2")
                .setConnectionTimeout("-2")
                .setFollowRedirects("false")
                .buildRequestConfig();
        assertNotNull(reqConfig);
        assertFalse(reqConfig.isRedirectsEnabled());
        assertEquals("-2", String.valueOf(reqConfig.getConnectTimeout()));
        assertEquals("-2", String.valueOf(reqConfig.getSocketTimeout()));
        assertNotNull(reqConfig.getProxy());
        assertEquals("myproxy.com", reqConfig.getProxy().getHostName());
        assertEquals("80", String.valueOf(reqConfig.getProxy().getPort()));
    }

    @Test
    public void buildNoProxyRoute() throws URISyntaxException, HttpException {
        RequestConfig reqConfig = requestConfigBuilder
                .buildRequestConfig();

        assertNotNull(reqConfig);
        assertTrue(reqConfig.isRedirectsEnabled());
        assertEquals("0", String.valueOf(reqConfig.getConnectTimeout()));
        assertEquals("0", String.valueOf(reqConfig.getSocketTimeout()));
        assertNull(reqConfig.getProxy());
    }

    @Test
    public void testBuildWithInvalidProxyPort(){
        final String invalidProxyPort = "invalidProxyPortText";
        final String expectedExceptionMessage ="Invalid value '"+ invalidProxyPort +"' for input 'proxyPort'. Valid Values: Integer values greater than 0";

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedExceptionMessage);
        requestConfigBuilder.setProxyHost("myproxy.com")
                .setProxyPort(invalidProxyPort)
                .buildRequestConfig();
    }
}

