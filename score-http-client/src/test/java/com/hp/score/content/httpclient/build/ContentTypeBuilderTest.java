/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.hp.score.content.httpclient.build;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 8/12/14
 */
public class ContentTypeBuilderTest {

    private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
    private static final String TEXT_PLAIN_CONTENT_TYPE = "text/plain";

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

        assertEquals(TEXT_PLAIN_CONTENT_TYPE, contentType.getMimeType().toString());
        assertEquals(Consts.ISO_8859_1.name(), contentType.getCharset().name());
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
