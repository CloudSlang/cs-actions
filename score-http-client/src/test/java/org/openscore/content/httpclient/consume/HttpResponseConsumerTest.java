/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.content.httpclient.consume;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;


/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IOUtils.class, HttpResponseConsumer.class})
public class HttpResponseConsumerTest {

    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
    private static final String RETURN_RESULT = "returnResult";
    private HttpResponseConsumer httpResponseConsumer;
    @Mock
    private HttpResponse httpResponseMock;
    @Mock
    private InputStream inputStreamMock;
    @Mock
    private BufferedReader bufferedReaderMock;
    @Mock
    private InputStreamReader inputStreamReaderMock;
    private Map<String, String> result;

    @Before
    public void setUp() {
        httpResponseConsumer = new HttpResponseConsumer();
        result = new HashMap<>();
    }

    @After
    public void tearDown() {
        httpResponseConsumer = null;
        result = null;
    }

    @Test
    public void consume() throws IOException {
        setHttpResponseEntity("text/plain;charset=");

        mockStatic(IOUtils.class);
        when(IOUtils.toString(inputStreamMock, Consts.ISO_8859_1.name())).thenReturn("doc");
        httpResponseConsumer
                .setHttpResponse(httpResponseMock)
                .setDestinationFile(null)
                .setResponseCharacterSet(null)
                .consume(result);
        assertEquals("doc", result.get(RETURN_RESULT));
    }

    @Test
    public void consumeWithContentType() throws IOException {
        setHttpResponseEntity(CONTENT_TYPE);

        mockStatic(IOUtils.class);
        when(IOUtils.toString(inputStreamMock, Consts.UTF_8.name())).thenReturn("doc");
        httpResponseConsumer
                .setHttpResponse(httpResponseMock)
                .setDestinationFile(null)
                .setResponseCharacterSet(null)
                .consume(result);
        assertEquals("doc", result.get(RETURN_RESULT));
    }

    @Test
    public void consumeWithDestinationFile() throws Exception {
        setHttpResponseEntity(CONTENT_TYPE);

        whenNew(InputStreamReader.class).withArguments(anyObject(), anyString()).thenReturn(inputStreamReaderMock);
        whenNew(BufferedReader.class).withArguments(inputStreamReaderMock).thenReturn(bufferedReaderMock);
        when(bufferedReaderMock.read((char[]) anyObject(), anyInt(), anyInt())).thenReturn(-1);

        httpResponseConsumer
                .setHttpResponse(httpResponseMock)
                .setDestinationFile("test.txt")
                .setResponseCharacterSet(null)
                .consume(result);
        assertNull(result.get(RETURN_RESULT));
    }

    private void setHttpResponseEntity(String contentType) {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(inputStreamMock);
        Header contentTypeHeader = new HeaderEntity("Content-Type", contentType);
        entity.setContentType(contentTypeHeader);
        when(httpResponseMock.getEntity()).thenReturn(entity);
    }

}
