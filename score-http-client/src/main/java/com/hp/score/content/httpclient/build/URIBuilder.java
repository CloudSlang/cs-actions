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