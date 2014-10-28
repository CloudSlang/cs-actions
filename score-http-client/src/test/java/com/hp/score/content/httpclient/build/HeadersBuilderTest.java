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

import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BufferedHeader;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/8/14
 */
public class HeadersBuilderTest {

    private static final String CONTENT_TYPE = "text/plain";

    @Test
    public void build() {
        ContentType contentType = ContentType.parse(CONTENT_TYPE);
        Header[] headers = new HeadersBuilder()
                .setHeaders("header1:value1\nheader2:value2")
                .setContentType(contentType)
                .buildHeaders();
        assertEquals(3, headers.length);
        assertThat(headers[0], instanceOf(BufferedHeader.class));
        BufferedHeader basicHeader = (BufferedHeader) headers[1];
        assertEquals("header2", basicHeader.getName());
        assertEquals("value2", basicHeader.getValue());
        BasicHeader contentTypeHeader = (BasicHeader) headers[2];
        assertEquals("Content-Type", contentTypeHeader.getName());
        assertEquals(CONTENT_TYPE, contentTypeHeader.getValue());
    }

    @Test
    public void buildWithNulls() {
        Header[] headers = new HeadersBuilder()
                .setHeaders(null)
                .setContentType(null)
                .buildHeaders();
        assertEquals(0, headers.length);
    }
}
