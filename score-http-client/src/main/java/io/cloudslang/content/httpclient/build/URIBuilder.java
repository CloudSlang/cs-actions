/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.httpclient.build;

import io.cloudslang.content.httpclient.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class URIBuilder {
    private String url;
    private String queryParams;
    private String queryParamsAreURLEncoded = "false";
    private String queryParamsAreFormEncoded = "true";

    public URIBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public URIBuilder setQueryParams(String queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public URIBuilder setQueryParamsAreURLEncoded(String queryParamsAreURLEncoded) {
        if (!StringUtils.isBlank(queryParamsAreURLEncoded)) {
            this.queryParamsAreURLEncoded = queryParamsAreURLEncoded;
        }
        return this;
    }

    public URIBuilder setQueryParamsAreFormEncoded(String queryParamsAreFormEncoded) {
        if (!StringUtils.isBlank(queryParamsAreFormEncoded)) {
            this.queryParamsAreFormEncoded = queryParamsAreFormEncoded;
        }
        return this;
    }

    public URI buildURI() {
        try {
            //validate as URL
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("the value '" + url + "' is not a valid URL", e);
        }
        org.apache.http.client.utils.URIBuilder uriBuilder;
        try {
            uriBuilder = new org.apache.http.client.utils.URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("the value '" + url + "' is not a valid URI", e);
        }

        boolean bEncodeQueryParams = !Boolean.parseBoolean(queryParamsAreURLEncoded);
        boolean bEncodeQueryParamsAsForm = Boolean.parseBoolean(queryParamsAreFormEncoded);

        if (!StringUtils.isEmpty(queryParams)) {
            try {
                if (bEncodeQueryParamsAsForm) {
                    uriBuilder.addParameters((List<NameValuePair>) Utils.urlEncodeMultipleParams(queryParams, bEncodeQueryParams));
                } else {
                    uriBuilder.setCustomQuery(Utils.urlEncodeQueryParams(queryParams, bEncodeQueryParams));
                }
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
            throw new IllegalArgumentException("could not build '" + HttpClientInputs.URL
                    + "' for " + url + " and queries " + queryParamsAreURLEncoded, e);
        }

    }
}