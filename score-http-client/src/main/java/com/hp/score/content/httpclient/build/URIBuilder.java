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
import com.hp.score.content.httpclient.HttpClientInputs;
import org.apache.http.NameValuePair;

import java.net.*;
import java.util.List;

public class URIBuilder {
    private String url;
    private String queryParams;
    private String queryParamsAreURLEncoded = "false";

    public URIBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public URIBuilder setQueryParams(String queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public URIBuilder setQueryParamsAreURLEncoded(String queryParamsAreURLEncoded) {
        if (!StringUtils.isEmpty(queryParamsAreURLEncoded)) {
            this.queryParamsAreURLEncoded = queryParamsAreURLEncoded;
        }
        return this;
    }

    public URI buildURI() {
        try {
            //validate as URL
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("could not parse " + url, e);
        }
        org.apache.http.client.utils.URIBuilder uriBuilder;
        try {
            uriBuilder = new org.apache.http.client.utils.URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not parse " + url + " as a URI", e);
        }

        boolean bEncodeQueryParams = !Boolean.parseBoolean(queryParamsAreURLEncoded);

        if (!StringUtils.isEmpty(queryParams)) {
            try {
                uriBuilder.addParameters((List<NameValuePair>) Utils.urlEncodeMultipleParams(queryParams, bEncodeQueryParams));
            } catch (IllegalArgumentException ie) {
                throw new IllegalArgumentException(
                        HttpClientInputs.QUERY_PARAMS_ARE_URLENCODED +
                                " is 'false' but queryParams are not properly encoded. "
                                + ie.getMessage(), ie);
            }

        }

        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not build '"+HttpClientInputs.URL
                    +"' for " + url + " and queries " + queryParamsAreURLEncoded, e);
        }

    }
}