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
package org.eclipse.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class HeadersBuilder {
    private String headers;
    private ContentType contentType;
    private Header entityContentType;

    public HeadersBuilder setHeaders(String headers) {
        this.headers = headers;
        return this;
    }

    public HeadersBuilder setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public HeadersBuilder setEntityContentType(Header entityContentType) {
        this.entityContentType = entityContentType;
        return this;
    }

    public List<Header> buildHeaders() {
        ArrayList<Header> headersArr = new ArrayList<>();
        if (!StringUtils.isEmpty(headers)) {
            BufferedReader in = new BufferedReader(new StringReader(headers));

            String str;
            try {
                while ((str = in.readLine()) != null) {
                    CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
                    charArrayBuffer.append(str);
                    headersArr.add(new BufferedHeader(charArrayBuffer));
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        if (entityContentType !=null) {
            headersArr.add(entityContentType);
        } else if (contentType != null && !contentType.toString().isEmpty()) {
            headersArr.add(new BasicHeader("Content-Type", contentType.toString()));
        }
        return headersArr;
    }
}