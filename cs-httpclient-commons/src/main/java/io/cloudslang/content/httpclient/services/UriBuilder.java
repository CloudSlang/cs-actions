/*
  * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.utils.UrlEncodeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.utils.Constants.UTF_8;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.QUERY_PARAMS_ARE_URLENCODED;

public class UriBuilder {

    public static URI getUri(HttpClientInputs httpClientInputs) throws URISyntaxException, IllegalArgumentException {
        URIBuilder uriBuilder = new URIBuilder(httpClientInputs.getHost());
        boolean bEncodeQueryParams = !Boolean.parseBoolean(httpClientInputs.getQueryParamsAreURLEncoded());
        boolean bEncodeQueryParamsAsForm = Boolean.parseBoolean(httpClientInputs.getQueryParamsAreFormEncoded());

        if (!StringUtils.isEmpty(httpClientInputs.getQueryParams())) {
            try {
                if (bEncodeQueryParamsAsForm) {
                    uriBuilder.addParameters((List<NameValuePair>) urlEncodeMultipleParams(httpClientInputs.getQueryParams(), bEncodeQueryParams));
                } else {
                    uriBuilder.setCustomQuery(urlEncodeQueryParams(httpClientInputs.getQueryParams(), bEncodeQueryParams));
                }
            } catch (IllegalArgumentException ie) {
                throw new IllegalArgumentException(
                        QUERY_PARAMS_ARE_URLENCODED +
                                " is 'false' but queryParams are not properly encoded. "
                                + ie.getMessage(), ie);
            }
        }
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not build 'URL' for"
                    + httpClientInputs.getHost() + " and queries " + httpClientInputs.getQueryParams(), e);
        }
    }

    private static List<? extends NameValuePair> urlEncodeMultipleParams(String params, boolean urlEncode) throws UrlEncodeException {
        List<BasicNameValuePair> list = new ArrayList<>();

        String[] pairs = params.split("&");
        for (String pair : pairs) {
            String[] nameValue = pair.split("=", 2);
            String name = nameValue[0];
            String value = nameValue.length == 2 ? nameValue[1] : null;

            if (!urlEncode) {
                try {
                    name = URLDecoder.decode(name, UTF_8);
                    if (value != null) {
                        value = URLDecoder.decode(value, UTF_8);
                    }
                } catch (UnsupportedEncodingException e) {
                    //never happens
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException ie) {
                    throw new UrlEncodeException(ie.getMessage(), ie);
                }
            }
            list.add(new BasicNameValuePair(name, value));
        }
        return list;
    }

    private static String urlEncodeQueryParams(String params, boolean urlEncode) throws UrlEncodeException {
        String encodedParams = params;
        if (!urlEncode) {
            try {
                encodedParams = URLDecoder.decode(params, UTF_8);
            } catch (UnsupportedEncodingException e) {
//                never happens
                throw new RuntimeException(e);
            } catch (IllegalArgumentException ie) {
                throw new UrlEncodeException(ie.getMessage(), ie);
            }
        }
        return encodedParams;
    }
}
