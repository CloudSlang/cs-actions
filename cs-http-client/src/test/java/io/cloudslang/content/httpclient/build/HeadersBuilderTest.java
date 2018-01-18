/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.httpclient.build;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BufferedHeader;
import org.junit.Test;

import java.util.List;

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
        List<Header> headers = new HeadersBuilder()
                .setHeaders("header1:value1\nheader2:value2")
                .setContentType(contentType)
                .buildHeaders();
        assertEquals(3, headers.size());
        assertThat(headers.get(0), instanceOf(BufferedHeader.class));
        BufferedHeader basicHeader = (BufferedHeader) headers.get(1);
        assertEquals("header2", basicHeader.getName());
        assertEquals("value2", basicHeader.getValue());
        BasicHeader contentTypeHeader = (BasicHeader) headers.get(2);
        assertEquals("Content-Type", contentTypeHeader.getName());
        assertEquals(CONTENT_TYPE, contentTypeHeader.getValue());
    }

    @Test
    public void buildWithNulls() {
        List<Header> headers = new HeadersBuilder()
                .setHeaders(null)
                .setContentType(null)
                .buildHeaders();
        assertEquals(0, headers.size());
    }
}
