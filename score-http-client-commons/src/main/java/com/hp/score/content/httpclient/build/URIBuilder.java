package com.hp.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import com.hp.score.content.httpclient.HttpClientInputs;
import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URIBuilder {
    private String url;
    private String queryParams;
    private String encodeQueryParams = "true";

    public URIBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public URIBuilder setQueryParams(String queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public URIBuilder setEncodeQueryParams(String encodeQueryParams) {
        if (!StringUtils.isEmpty(encodeQueryParams)) {
            this.encodeQueryParams = encodeQueryParams;
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

        boolean bEncodeQueryParams = Boolean.parseBoolean(encodeQueryParams);

        if (!StringUtils.isEmpty(queryParams)) {
            try {
                uriBuilder.addParameters((List<NameValuePair>) Utils.urlEncodeMultipleParams(queryParams, bEncodeQueryParams));
            } catch (IllegalArgumentException ie) {
                throw new IllegalArgumentException(
                        HttpClientInputs.ENCODE_QUERY_PARAMS +
                                " is 'false' but queryParams are not properly encoded. "
                                + ie.getMessage(), ie);
            }

        }

        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not build '"+HttpClientInputs.URL
                    +"' for " + url + " and queries " + encodeQueryParams, e);
        }

    }
}