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

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;

import java.nio.charset.UnsupportedCharsetException;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 8/12/14
 */
public class ContentTypeBuilder {

    private String contentType;
    private String requestCharacterSet;

    public ContentTypeBuilder setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public ContentTypeBuilder setRequestCharacterSet(String requestCharacterSet) {
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            this.requestCharacterSet = requestCharacterSet;
        }
        return this;
    }

    public ContentType buildContentType() {
        ContentType parsedContentType = null;
        if (StringUtils.isNotBlank(contentType)) {
            try {
                parsedContentType = ContentType.parse(contentType);
            } catch (ParseException | UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse input '"
                        + HttpClientInputs.CONTENT_TYPE + "'. " + e.getMessage(), e);
            }

            if (!StringUtils.isEmpty(requestCharacterSet)) {
                try {
                    parsedContentType = parsedContentType.withCharset(requestCharacterSet);
                } catch (UnsupportedCharsetException e) {
                    throw new IllegalArgumentException("Could not parse input '" + HttpClientInputs.REQUEST_CHARACTER_SET
                            + "'. " + e.getMessage(), e);
                }
            }
        }
        return parsedContentType;
    }
}
