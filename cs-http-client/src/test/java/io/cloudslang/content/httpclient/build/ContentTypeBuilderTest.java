/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.build;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 8/12/14
 */
public class ContentTypeBuilderTest {

    private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";

    @Test
    public void buildContentType() {
        ContentType contentType = new ContentTypeBuilder()
                .setContentType(APPLICATION_JSON_CONTENT_TYPE)
                .setRequestCharacterSet(Consts.UTF_8.name()).buildContentType();

        assertEquals(APPLICATION_JSON_CONTENT_TYPE, contentType.getMimeType().toString());
        assertEquals(Consts.UTF_8.name(), contentType.getCharset().name());
    }

    @Test
    public void buildContentTypeWithDefaultValues() {
        ContentType contentType = new ContentTypeBuilder()
                .buildContentType();

        assertNull(contentType);
    }

    @Test
    public void buildContentTypeWithRequestCharacterSet() {
        ContentType contentType = new ContentTypeBuilder()
                .setContentType("application/json; charset=UTF-8")
                .setRequestCharacterSet(Consts.ISO_8859_1.name()).buildContentType();

        assertEquals(APPLICATION_JSON_CONTENT_TYPE, contentType.getMimeType().toString());
        assertEquals(Consts.ISO_8859_1.name(), contentType.getCharset().name());
    }

}
