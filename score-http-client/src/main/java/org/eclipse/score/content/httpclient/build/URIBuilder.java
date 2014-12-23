/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.eclipse.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.score.content.httpclient.HttpClientInputs;
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