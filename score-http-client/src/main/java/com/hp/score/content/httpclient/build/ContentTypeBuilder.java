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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import com.hp.score.content.httpclient.HttpClientInputs;

import java.nio.charset.UnsupportedCharsetException;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 8/12/14
 */
public class ContentTypeBuilder {

    private ContentType defaultContentType = ContentType.TEXT_PLAIN;
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
        String contentType = this.contentType;
        String requestCharacterSet = this.requestCharacterSet;
        ContentType parsedContentType;
        if (StringUtils.isEmpty(contentType)) {
            parsedContentType = defaultContentType;
        } else {
            try {
                parsedContentType = ContentType.parse(contentType);
            } catch (ParseException | UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse input '"
                        + HttpClientInputs.CONTENT_TYPE+"'. " + e.getMessage(), e);
            }
        }
        //do not override contentType provide by user
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            try {
                parsedContentType = parsedContentType.withCharset(requestCharacterSet);
            } catch (UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse input '"+HttpClientInputs.REQUEST_CHARACTER_SET
                        +"'. " + e.getMessage(), e);
            }
        }
        return parsedContentType;
    }
}
