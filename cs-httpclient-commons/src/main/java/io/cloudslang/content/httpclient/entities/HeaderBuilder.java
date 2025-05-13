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
            if (!StringUtils.isEmpty(httpClientInputs.getHeaders())) {
                if (!httpClientInputs.getContentType().isEmpty()) {
                    String[] headerList = httpClientInputs.getHeaders().split(COMMA);
                    boolean counter = false;
                    for (String eachHeader : headerList) {
                        if (eachHeader.toLowerCase().contains(CONTENT_TYPE.toLowerCase())) {
                            httpRequest.setHeader(new HeaderObj(CONTENT_TYPE, httpClientInputs.getContentType()));
                            counter = true;
                        } else {
                            String[] headerListSingle = eachHeader.split(COLON);
                            httpRequest.setHeader(new HeaderObj(headerListSingle[0].trim(), headerListSingle[1]));
                        }
                    }
                    if (!counter)
                        httpRequest.setHeader(new HeaderObj(CONTENT_TYPE, httpClientInputs.getContentType()));
                } else {
                    String[] listheader = httpClientInputs.getHeaders().split(COMMA);
                    for (String list : listheader) {
                        String[] headerListSingle = list.split(COLON);
                        httpRequest.setHeader(new HeaderObj(headerListSingle[0].trim(), headerListSingle[1]));
                    }
                }
            } else if (!httpClientInputs.getContentType().isEmpty())
                httpRequest.setHeader(new HeaderObj(CONTENT_TYPE, httpClientInputs.getContentType()));
        } catch (Exception e) {
            throw new IllegalArgumentException(EXCEPTION_INVALID_HEADER_FORMAT);
        }
    }
}

