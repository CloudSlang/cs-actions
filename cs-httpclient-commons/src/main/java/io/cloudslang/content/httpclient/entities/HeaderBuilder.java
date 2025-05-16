/*
 * Copyright 2022-2025 Open Text
 * This program and the accompanying materials
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


package io.cloudslang.content.httpclient.entities;

import io.cloudslang.content.httpclient.utils.HeaderObj;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

import static io.cloudslang.content.httpclient.utils.Constants.*;

public class HeaderBuilder {

    public static void headerBuiler(HttpUriRequestBase httpRequest, HttpClientInputs httpClientInputs) {
        try {
            boolean contentTypeSet = false;

            String rawHeaders = httpClientInputs.getHeaders();
            String contentType = httpClientInputs.getContentType();

            if (!StringUtils.isEmpty(rawHeaders)) {
                // Split by any line break (handles \n, \r, or \r\n)
                String[] headerLines = rawHeaders.split("\\R+");

                for (String line : headerLines) {
                    if (line == null || line.trim().isEmpty()) continue;

                    int colonIndex = line.indexOf(':');
                    if (colonIndex == -1) {
                        throw new IllegalArgumentException(EXCEPTION_INVALID_HEADER_FORMAT);
                    }

                    String name = line.substring(0, colonIndex).trim();
                    String value = line.substring(colonIndex + 1).trim();

                    // If Content-Type header is in the input, override its value
                    if (name.equalsIgnoreCase(CONTENT_TYPE) && !contentType.isEmpty()) {
                        value = contentType;
                        contentTypeSet = true;
                    } else if (name.equalsIgnoreCase(CONTENT_TYPE)) {
                        contentTypeSet = true;
                    }

                    httpRequest.setHeader(new HeaderObj(name, value));
                }
            }

            // If Content-Type wasn't set in headers but is available in input, set it explicitly
            if (!contentTypeSet && !StringUtils.isEmpty(contentType)) {
                httpRequest.setHeader(new HeaderObj(CONTENT_TYPE, contentType));
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(EXCEPTION_INVALID_HEADER_FORMAT);
        }
    }
}

